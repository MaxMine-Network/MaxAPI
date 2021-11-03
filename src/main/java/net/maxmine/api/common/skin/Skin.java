package net.maxmine.api.common.skin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class Skin {

    private String skinName;
    private String playerUUID;
    private String value;
    private String signature;
    private long timestamp;

    public String[] getData() {
        return new String[] { playerUUID, value, signature };
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - timestamp > TimeUnit.HOURS.toMillis(12L);
    }
}
