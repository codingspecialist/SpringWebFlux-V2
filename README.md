# SpringWebFlux-V2

## SSE 프로토콜을 이용한 WebFlux 체험기

### 일반적인 WebFlux
- Flux (한번에 여러개 리턴) , Mono (한개 리턴)
- Flux.interval 리턴시 stream이 적용됨. APPLICATION_STREAM_JSON_VALUE 사용할 MIME 타입

### 반응형 리엑터
- Processor 필요함. EmiitterProcessor, FluxProcessor 등이 있었는데 지금 다 deprecate됨.
- 현재는 Sinks.Many<T> sink = Sinks.many().multicast().onBackpressureBuffer(); 사용

```java
// many().multicast(): 새로 푸시 된 데이터 만 구독자에게 전송하여 배압을 준수하는 싱크 ( "구독자의 구독 후"에서처럼 새로 푸시 됨).
// many().unicast(): 위와 동일하며 첫 번째 구독자 레지스터가 버퍼링되기 전에 푸시 된 데이터가 왜곡됩니다.
// many().replay(): 푸시 된 데이터의 지정된 기록 크기를 새 구독자에게 재생 한 다음 새 데이터를 계속해서 실시간으로 푸시하는 싱크입니다.
// one(): 구독자에게 단일 요소를 재생하는 싱크
// empty(): 가입자에게만 터미널 신호를 재생하지만 (오류 또는 완료) 여전히 Mono<T>(일반 유형에주의) 로 볼 수있는 싱크 <T>.
```

- SSE를 통해 반응형 구독을 하려면 return 타입이 Flux<ServerSentEvent<T>> 타입이어야 함.
- 리턴시 sink.asFlux().map(e->ServerSentEvent.builder(e).build()); 사용
- MIME타입은 TEXT_EVENT_STREAM_VALUE

### R2DBC 사용해서 V2 만들어볼 계획임.
