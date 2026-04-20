package com.ktdsuniversity.edu.members.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ktdsuniversity.edu.members.vo.MembersVO;
import com.ktdsuniversity.edu.members.vo.request.LoginVO;
import com.ktdsuniversity.edu.members.vo.request.MemberSearchVO;
import com.ktdsuniversity.edu.members.vo.request.RegistVO;
import com.ktdsuniversity.edu.members.vo.request.UpdateVO;

@Mapper
public interface MembersDao {

	int insertNewMember(RegistVO registVO);

	MembersVO selectMemberByEmail(String email);

	int updateMemberByEmail(UpdateVO updateVO);

	int deleteMemberByEmail(String email);

	int selectMembersCount(MemberSearchVO membersSearchVO);

	List<MembersVO> selectMembersList(MemberSearchVO membersSearchVO);

	int updateIncreaseLoginFailCount(String email);

	int updateBlock(String email);

	int updateSuccessLogin(LoginVO loginVO);

	List<String> selectMemberRolesByEmail(String email);

}
