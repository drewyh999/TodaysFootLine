package cn.thundergaba.todaysfootline;

public class ToutiaoVideo extends ToutiaoItem{

        private String play_url;

        private boolean is_liked;//是否被当前用户点赞

        private String cover_url;

        public String getCover_url() {
            return cover_url;
         }

         public void setCover_url(String cover_url) {
              this.cover_url = cover_url;
        }

        public String getPlay_url() {
            return play_url;
        }

        public void setPlay_url(String play_url) {
            this.play_url = play_url;
        }

        public boolean isIs_liked() {
            return is_liked;
        }

        public void setIs_liked(boolean is_liked) {
            this.is_liked = is_liked;
        }

}
