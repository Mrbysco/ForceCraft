package qa;
import com.google.gson.GsonBuilder;
import com.mrbysco.forcecraft.handler.infusion.TimeHandler;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import java.nio.file.*;import java.util.*;import java.util.function.Consumer;
@Mod("itv_force_time_qa") public class TimeQa {
 public TimeQa(){NeoForge.EVENT_BUS.addListener(this::register);}
 void register(RegisterCommandsEvent e){e.getDispatcher().register(Commands.literal("timeqa").requires(s->s.getEntity()==null && s.getServer().getLocalIp().equals("127.0.0.1")).executes(c->{var out=new LinkedHashMap<String,Object>();var cases=new ArrayList<String>(); Consumer<ChunkEvent.Load> listener=null;try{
 var server=c.getSource().getServer();if(!server.getPlayerList().getPlayers().isEmpty())throw new AssertionError("Real players forbidden");var level=server.overworld();
 var field=TimeHandler.class.getDeclaredField("TRACKED_BLOCKS");field.setAccessible(true);
 @SuppressWarnings("unchecked") var map=(Map<ResourceKey<Level>,Set<BlockPos>>)field.get(null);map.clear();
 TimeHandler.onLevelTick(new LevelTickEvent.Pre(()->true,level));cases.add("empty_tracking_safe");
 var a=new BlockPos(16000,80,16000);var b=a.offset(1,0,0);var added=a.offset(2,0,0);
 if(level.hasChunk(a.getX()>>4,a.getZ()>>4))throw new AssertionError("Must begin unloaded");
 TimeHandler.trackBlock(level,a);TimeHandler.trackBlock(level,b);TimeHandler.trackBlock(level,a);
 if(map.get(level.dimension()).size()!=2)throw new AssertionError("Duplicate tracking");cases.add("duplicate_tracking_idempotent");
 boolean[] fired={false};listener=ev->{if(ev.getLevel()==level && !fired[0]){fired[0]=true;TimeHandler.trackBlock(level,added);}};
 NeoForge.EVENT_BUS.addListener(listener);
 try {TimeHandler.onLevelTick(new LevelTickEvent.Pre(()->true,level));}
 catch(ConcurrentModificationException ex){out.put("state","original_crash_reproduced");out.put("exception",ex.getClass().getSimpleName());}
 if(!fired[0])throw new AssertionError("Chunk-load callback did not execute");cases.add("actual_chunk_load_reentered_trackBlock");
 if(!out.containsKey("state")){
 if(!map.get(level.dimension()).equals(Set.of(added)))throw new AssertionError("Newly tracked entry must survive until next tick: "+map.get(level.dimension()).size());cases.add("new_entry_deferred_existing_missing_removed");
 TimeHandler.onLevelTick(new LevelTickEvent.Pre(()->true,level));if(!map.get(level.dimension()).isEmpty())throw new AssertionError("Missing new entry not pruned");cases.add("next_tick_prunes_missing_entry");
 out.put("state","passed");}
 map.clear();
 }catch(Throwable ex){out.put("state","failed");out.put("error",ex.toString());ex.printStackTrace();}finally{if(listener!=null)NeoForge.EVENT_BUS.unregister(listener);}
 out.put("cases",cases);out.put("production_access",false);
 try{Files.writeString(Path.of("time-qa.json"),new GsonBuilder().setPrettyPrinting().create().toJson(out));}catch(Exception ex){throw new RuntimeException(ex);}c.getSource().sendSuccess(()->Component.literal("TIME_QA "+out.get("state")),false);return 1;}));}
}
