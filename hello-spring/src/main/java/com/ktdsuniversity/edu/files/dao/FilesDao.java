package com.ktdsuniversity.edu.files.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ktdsuniversity.edu.files.vo.request.SearchFileGroupVO;
import com.ktdsuniversity.edu.files.vo.request.SearchFileVO;
import com.ktdsuniversity.edu.files.vo.request.UploadVO;
import com.ktdsuniversity.edu.files.vo.response.DownloadVO;

@Mapper
public interface FilesDao {

	int insertAttachFile(UploadVO uploadVO);

	DownloadVO selectFilesByFileGroupIdAndFileNum(SearchFileVO searchFileVO);

	List<String> selectFilePathByFileGroupIdAndFileNums(SearchFileGroupVO searchFileGroupVO);

	int deleteFilesByFileGroupIdAndFileNums(SearchFileGroupVO searchFileGroupVO);

	List<String> selectFilePathByFileGroupId(String id);

	int deleteFileByFileGroupId(String id);

	String selectNewFileGroupId();

	int insertFileGroupId(String fileGroupId);
	
	

	int deleteFileByFilePath(String filePath);

	int deleteFileGroupByFilePath(String filePath);

}
