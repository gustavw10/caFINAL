package facades;

import dtos.TagDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class GetTagDTO implements Callable<TagDTO>{
  String url;
  GetTagDTO(String url){
    this.url = url;
  }
  @Override
  public TagDTO call() throws Exception {
    TagCounter tc = new TagCounter(url);
    tc.doCount();
    return new TagDTO(tc);
  }
}

public class ScraperFacade {


    public static List<TagDTO> runParrallel() throws InterruptedException, ExecutionException  {
        ExecutorService executor = Executors.newCachedThreadPool();
        String[] arr = {"https://www.amazon.com", "https://www.google.com", "https://www.twitch.tv/", "https://www.youtube.com"}; 
        List<TagDTO> result = new ArrayList<>();
        List<Future<TagDTO>> futureList = new ArrayList<>();
        
        for(String url : arr){
            //Callable<TagDTO> dto = new GetTagDTO(url);
            Future<TagDTO> future = executor.submit(new GetTagDTO(url));
            futureList.add(future);
        }
        for(Future<TagDTO> future : futureList){
        result.add(future.get());
        }
        
        return result;
    } 

   
}
