package net.htwater.whale.entity.param;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author Jokki
 * @since Created in 下午7:41 18-9-21
 */
@Data
public class ContourRainsParams {
    @NotNull
    private String stations;
    @NotBlank
    private String startTime;
    @NotBlank
    private String endTime;
    @NotNull
    int type;
}
