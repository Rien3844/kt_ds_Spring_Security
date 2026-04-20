package com.ktdsuniversity.edu.members.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktdsuniversity.edu.exceptions.HelloSpringException;
import com.ktdsuniversity.edu.members.dao.MembersDao;
import com.ktdsuniversity.edu.members.helpers.SHA256Util;
import com.ktdsuniversity.edu.members.vo.MembersVO;
import com.ktdsuniversity.edu.members.vo.request.MemberSearchVO;
import com.ktdsuniversity.edu.members.vo.request.RegistVO;
import com.ktdsuniversity.edu.members.vo.request.UpdateVO;
import com.ktdsuniversity.edu.members.vo.response.SearchResultVO;

@Service
public class MembersServiceImpl implements MembersService {

	@Autowired
	private MembersDao membersDao;
	
	@Transactional
	@Override
	public boolean createNewMember(RegistVO registVO) {
		
		MembersVO membersVO = this.membersDao.selectMemberByEmail(registVO.getEmail());
		if (membersVO != null) {
			throw new HelloSpringException("이미 사용중인 이메일입니다.", "members/regist", registVO);
		}
		
		// 암호화를 위한 비밀키 생성.
		String newSalt = SHA256Util.generateSalt();
		String usersPassword = registVO.getPassword();
		// 사용자가 입력한 비밀번호를 newSalt를 이용해 암호화
		// 비밀번호와 newSalt의 값이 일치하면, 항상 같은 값의 암호화 결과가 생성된다.
		usersPassword = SHA256Util.getEncrypt(usersPassword, newSalt);
		
		// 비밀키 저장.
		registVO.setSalt(newSalt);
		// 암호화된 비밀번호 저장.
		registVO.setPassword(usersPassword);
		
		int insertCount = this.membersDao.insertNewMember(registVO);
		return insertCount == 1;
	}

	@Transactional
	@Override
	public MembersVO findMemberByEmail(String email) {
		MembersVO searchResult = this.membersDao.selectMemberByEmail(email);
		return searchResult;
	}

	@Transactional
	@Override
	public boolean updateMemberByEmail(UpdateVO updateVO) {
		int updateCount = this.membersDao.updateMemberByEmail(updateVO);
		return updateCount == 1;
	}

	@Transactional
	@Override
	public boolean deleteMemberByEmail(String email) {
		int deleteCount = this.membersDao.deleteMemberByEmail(email);
		return deleteCount == 1;
	}

	@Override
	public SearchResultVO findMembersList(MemberSearchVO membersSearchVO) {
		SearchResultVO result = new SearchResultVO();
		int searchCount = this.membersDao.selectMembersCount(membersSearchVO);
		result.setCount(searchCount);
		
		membersSearchVO.computePagination(searchCount);
		
		if (searchCount == 0) {
			return result;
		}
		
		List<MembersVO> searchResult = this.membersDao.selectMembersList(membersSearchVO);
		result.setResult(searchResult);
		
		return result;
	}

}
