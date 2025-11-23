package pl.wsb.fitnesstracker.workoutsession;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.wsb.fitnesstracker.training.api.Training;

@Entity
@Table(name = "Workout_Session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private int id;

    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @Column(name = "timestamp")
    private String timestamp;
    @Column(name = "startLatitude")
    private double startLatitude;
    @Column(name = "startLongitude")
    private double startLongitude;
    @Column(name = "endLatitude")
    private double endLatitude;
    @Column(name = "endLongitude")
    private double endLongitude;
    @Column(name = "altitude")
    private double altitude;

    public WorkoutSession(Training training, String timestamp, double startLatitude, double startLongitude, double endLatitude, double endLongitude, double altitude) {
        this.training = training;
        this.timestamp = timestamp;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.altitude = altitude;
    }
}
