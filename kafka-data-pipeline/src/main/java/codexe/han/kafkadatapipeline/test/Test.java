package codexe.han.kafkadatapipeline.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
    public static void main(String[] args) {
      log.info("test res is {}", Boolean.valueOf("true") && Boolean.valueOf("true"));
      log.info("test res is {}", Boolean.valueOf("false") && Boolean.valueOf("true"));
      log.info("test res is {}", Boolean.valueOf("false") && Boolean.valueOf("false"));
      log.info("test res is {}", Boolean.valueOf("true") && Boolean.valueOf("false"));
    }
}
