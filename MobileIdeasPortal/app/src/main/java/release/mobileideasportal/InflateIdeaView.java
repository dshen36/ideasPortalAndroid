package release.mobileideasportal;

import android.view.View;

/**
 * Created by brich200 on 7/13/15.
 */
public class InflateIdeaView {
        private View view, parentView;
        private int id;
        private Idea idea;

        public InflateIdeaView(){
            view = null;
            parentView = null;
            id = 0;
            idea = new Idea();
        }

        public InflateIdeaView(View v, View parentView, double markerTime){
            setView(v);
            setId(id);
        }

        public View getView(){return view;}
        public View getParentView() {return parentView;}
        public int getId(){return id;}
        public Idea getIdea(){return idea;}

        public void setView(View v) {
            view = v;
        }

        public void setParentView(View p) {
            parentView = p;
        }

        public void setId(int id){
            this.id = id;
        }

        public void setIdea(Idea idea) {this.idea = idea;}
}
