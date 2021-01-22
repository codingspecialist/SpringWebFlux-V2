package com.cos.reactivestream;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

@RequestMapping("/test1")
@RestController
public class TestController1 {

    final FluxProcessor processor;
    final FluxSink sink;
    final AtomicLong counter;

    public TestController1() {
        this.processor = DirectProcessor.create().serialize();
        this.sink = processor.sink();
        this.counter = new AtomicLong();
    }

    @GetMapping("/send")
    public void test() {
        sink.next("Hello World #" + counter.getAndIncrement());
    }

    @GetMapping(value="/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent> sse() {
        return processor.map(e -> ServerSentEvent.builder(e).build());
    }
}
