package wang.aemon.ss.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Menu {
    private Long id;
    private String pattern;
    private List<Role> roles;
}
