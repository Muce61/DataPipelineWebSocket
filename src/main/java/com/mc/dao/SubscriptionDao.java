package com.mc.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @Description
 * @Author muse
 * @Date 2024/07/24
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDao {
    @JsonProperty("type")
    private String type;
    @JsonProperty("api-secret")
    private String ApiSecret;
    @JsonProperty("topics")
    private String topics;
}
