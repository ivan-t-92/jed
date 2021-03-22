public class ModelHi {
    private ViewHi view;
    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
        if (view != null) {
            view.updateLabel("Hello " + userName);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setView(ViewHi view) {
        this.view = view;
    }
}
