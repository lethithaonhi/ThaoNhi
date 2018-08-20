package vn.hcmute.edu.vn.cuoiky.foody.Controller;

import vn.hcmute.edu.vn.cuoiky.foody.Model.Member;

public class RegisterController {
    Member member;

    public RegisterController(){
        member = new Member();
    }

    public void ThemThongTinThanhVienController(Member member, String uid){
        member.ThemThongTinThanhVien(member, uid);
    }
}


