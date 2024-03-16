package com.backend.blooming.stamp.presentation.dto.response;

import com.backend.blooming.stamp.application.dto.ReadAllStampDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ReadAllStampResponse(Map<Integer, List<StampInfoResponse>> stamps) {

    public static ReadAllStampResponse from(final ReadAllStampDto readAllStampDto) {
        final List<StampInfoResponse> stampInfos = readAllStampDto.stamps()
                                                                  .stream()
                                                                  .map(StampInfoResponse::from)
                                                                  .toList();
        final Map<Integer, List<StampInfoResponse>> stamps =
                stampInfos.stream()
                          .collect(Collectors.groupingBy(StampInfoResponse::day));

        return new ReadAllStampResponse(stamps);
    }

    public record StampInfoResponse(
            Long userId,
            String name,
            String color,
            String message,
            int day
    ) {

        public static StampInfoResponse from(final ReadAllStampDto.StampDto stampDto) {
            return new StampInfoResponse(
                    stampDto.userId(),
                    stampDto.name(),
                    stampDto.color().getCode(),
                    stampDto.message(),
                    stampDto.day()
            );
        }
    }
}
