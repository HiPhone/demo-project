package adapter;

public class PoliceCarAdapter extends CarController {

    private PoliceLight policeLight;
    private PoliceSound policeSound;

    public PoliceCarAdapter() {
        policeLight = new PoliceLight();
        policeSound = new PoliceSound();
    }

    @Override
    public void light() {
        policeLight.alarmLight();
    }

    @Override
    public void voice() {
        policeSound.alarmSound();
    }
}
