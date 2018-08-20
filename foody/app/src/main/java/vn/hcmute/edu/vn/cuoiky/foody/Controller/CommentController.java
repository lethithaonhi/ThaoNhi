package vn.hcmute.edu.vn.cuoiky.foody.Controller;

import java.util.List;

import vn.hcmute.edu.vn.cuoiky.foody.Model.Comment;

public class CommentController {
    Comment comment;

    public CommentController(){
        comment = new Comment();
    }

    public void  ThemBinhLuan(String maquan, Comment comment, List<String> list){
        comment.ThemBinhLuan(maquan, comment, list);
    }
}
