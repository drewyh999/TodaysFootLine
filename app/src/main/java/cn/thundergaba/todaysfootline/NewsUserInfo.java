package cn.thundergaba.todaysfootline;

public class NewsUserInfo {
    private String name;

    private String avatar_url;

    public NewsUserInfo(String name, String avatar_url) {
        this.name = name;
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}