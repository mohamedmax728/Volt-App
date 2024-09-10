package Volt.example.Volt.Interaction.Application.Dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class FollowerListDto {
    private Set<Integer> followers = new HashSet<Integer>();
    private Set<Integer> unFollowers = new HashSet<Integer>();
}
