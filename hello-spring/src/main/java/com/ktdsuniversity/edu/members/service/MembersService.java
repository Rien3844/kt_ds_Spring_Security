package com.ktdsuniversity.edu.members.service;

import com.ktdsuniversity.edu.members.vo.MembersVO;
import com.ktdsuniversity.edu.members.vo.request.LoginVO;
import com.ktdsuniversity.edu.members.vo.request.RegistVO;
import com.ktdsuniversity.edu.members.vo.request.UpdateVO;
import com.ktdsuniversity.edu.members.vo.response.SearchResultVO;

public interface MembersService {

	boolean createNewMember(RegistVO registVO);

	MembersVO findMemberByEmail(String email);

	boolean updateMemberByEmail(UpdateVO updateVO);

	boolean deleteMemberByEmail(String email);

	SearchResultVO findMembersList();

	MembersVO findMemberByEmailAndPassword(LoginVO loginVO);

}
