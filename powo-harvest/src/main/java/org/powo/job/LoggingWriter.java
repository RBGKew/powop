package org.powo.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class LoggingWriter implements ItemWriter<Object> {
  private static final Logger log = LoggerFactory.getLogger(LoggingWriter.class);

  @Override
  public void write(List<? extends Object> items) throws Exception {
    log.info("writing {} items", items.size());
    if (items.size() > 0) {
      log.info("first item in chunk {}", items.get(0));
    }
  }
}