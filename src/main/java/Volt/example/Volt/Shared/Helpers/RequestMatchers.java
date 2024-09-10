package Volt.example.Volt.Shared.Helpers;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMatchers {

    public static List<RequestMatcher> getRequestMatchersFromPatterns(List<String> patterns) {
        return patterns.stream()
                .map(pattern -> new AntPathRequestMatcher(pattern))
                .collect(Collectors.toList());
    }

//    public static RequestMatcher[] getAllAuthorizedByUser(){
//        var list = getRequestMatchersFromPatterns(List.of(
//                "/api/auth/resetPassword/**",
//                "**/channel/**"
//        ));
//        return list.toArray(new RequestMatcher[0]);
//    }

    public static RequestMatcher[] getRequestMatchersFromPatterns() {
        List<String> patterns = List.of(
                "/api/auth/resetPassword/**",
                "/api/mobile/contentManagement/channel/**",
                "/api/mobile/interaction/follower/**"
        );
         var list = patterns.stream()
                .map(pattern -> new AntPathRequestMatcher(pattern))
                .collect(Collectors.toList());

        return list.toArray(new RequestMatcher[0]);
    }

    public static RequestMatcher[] getAllPermit() {
        List<String> patterns = List.of(
                "/api/auth/**",
                "/api/mobile/content_management/channel/channel/get/**"
        );
        var list = patterns.stream()
                .map(pattern -> new AntPathRequestMatcher(pattern))
                .collect(Collectors.toList());

        return list.toArray(new RequestMatcher[0]);
    }
}
