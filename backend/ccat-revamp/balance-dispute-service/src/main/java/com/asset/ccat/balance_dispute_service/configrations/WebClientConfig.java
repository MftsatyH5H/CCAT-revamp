package com.asset.ccat.balance_dispute_service.configrations;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


@Configuration
public class WebClientConfig {

  @Autowired
  Properties properties;

  @Bean
  WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  public WebClient webClient() {
    return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
            .codecs(configure -> configure
                .defaultCodecs()
                .maxInMemorySize(16 * 1024 * 1024)).build())
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
            .responseTimeout(Duration.ofSeconds(properties.getResponseTimeout()))
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectionTimeout())
            .doOnConnected(connection -> connection.addHandler(
                    new ReadTimeoutHandler(properties.getReadTimeout()))
                .addHandler(new WriteTimeoutHandler(properties.getWriteTimeout())))
        )).build();
  }
}
