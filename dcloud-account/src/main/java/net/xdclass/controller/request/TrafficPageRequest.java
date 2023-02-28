package net.xdclass.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 刘森飚
 * @since 2023-02-07
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficPageRequest {

    private  int page;

    private int size;
}
