package pl.wsb.fitnesstracker.training.api;

import io.micrometer.common.lang.Nullable;
import pl.wsb.fitnesstracker.training.internal.ActivityType;
import pl.wsb.fitnesstracker.user.api.UserDto;

import java.util.Date;

public record TrainingDto(@Nullable Long id,
                          UserDto user,
                          Date startTime,
                          Date endTime,
                          ActivityType activityType,
                          double distance,
                          double averageSpeed) {
}
