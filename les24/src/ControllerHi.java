public class ControllerHi {

    private final ModelHi model;

    public ControllerHi(ModelHi model) {
        this.model = model;
    }

    public void setName(String name) {
        if (!name.isEmpty()) {
            model.setUserName(Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase());
        }
    }

}
