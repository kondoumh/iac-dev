package com.example.consolexml;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.StopWatch;

@ShellComponent
public class ShellController { 

  @ShellMethod(value = "hello", key = "entry")
  public void entryPoint() {
    var stopWatch = new StopWatch();
    stopWatch.start();
		var marshaler = new HogeMarshaler();
		marshaler.marshal();
		var hoge = marshaler.unmarshal();
		System.out.println(hoge);
    stopWatch.stop();
    System.out.println(stopWatch.prettyPrint());
  }
}
