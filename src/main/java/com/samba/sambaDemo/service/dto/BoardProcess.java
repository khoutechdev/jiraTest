package com.samba.sambaDemo.service.dto;

import com.samba.sambaDemo.domain.Board;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BoardProcess {

    private List<Board> values;
}
