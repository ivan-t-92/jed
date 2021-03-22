class Hi {

    public static void main(String[] args) {
        ModelHi model = new ModelHi();
        ControllerHi controller = new ControllerHi(model);
        ViewHi view = new ViewHi(controller);
        model.setView(view);

        view.setVisible(true);
    }
}