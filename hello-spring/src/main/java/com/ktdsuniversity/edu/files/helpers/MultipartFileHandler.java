package com.ktdsuniversity.edu.files.helpers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ktdsuniversity.edu.files.dao.FilesDao;
import com.ktdsuniversity.edu.files.vo.request.UploadVO;

@Component
public class MultipartFileHandler {

	private static final Logger logger = LoggerFactory.getLogger(MultipartFileHandler.class);

	@Autowired
	private FilesDao filesDao;

	public String upload(List<MultipartFile> attachFiles, String fileGroupId) {
		if (attachFiles != null && attachFiles.size() > 0) {

			for (int i = 0; i < attachFiles.size(); i++) {

				// 업로드를 하지 않았는데 했다고 판단한 경우에는 다음 반복으로 넘어가라.
				if (attachFiles.get(i).isEmpty()) {
					continue;
				}

//			for (MultipartFile uploadedFile: attachFiles) {

				// UUID ==> 현재 시간을 기준으로 난수화 된 값을 가져오는 방법.
				// 전세계에서 동시에 발급받더라도 절대로 중복이 일어나지 않는다!
				String obfuscateName = UUID.randomUUID().toString();

				String homepath = System.getProperty("user.home");

				// 업로드한 파일이 서버컴퓨터의 파일 시스템에 저장되도록 한다.
				File storeFile = new File(homepath + "/uploadFiles", obfuscateName);
				// C:\\uploadFiles 폴더가 없으면 생성해라!
				if (!storeFile.getParentFile().exists()) {
					storeFile.getParentFile().mkdirs();
				}
				try {
					attachFiles.get(i).transferTo(storeFile);

					// FILES 테이블에 첨부파일 데이터를 INSERT
					UploadVO uploadVO = new UploadVO();

					String filename = attachFiles.get(i).getOriginalFilename();
					String ext = filename.substring(filename.lastIndexOf(".") + 1);

					uploadVO.setFileGroupId(fileGroupId);
					uploadVO.setObfuscateName(obfuscateName);
					uploadVO.setDisplayName(filename);
					uploadVO.setExtendName(ext);
					uploadVO.setFileLength(storeFile.length());
					uploadVO.setFilePath(storeFile.getAbsolutePath());

					this.filesDao.insertAttachFile(uploadVO);
				} catch (IllegalStateException | IOException e) {
					logger.error("파일 업로드 중 에러 발생!", e);
				}
			}

			return fileGroupId;
		}

		return null;
	}

	/**
	 * 
	 * @param attachFiles
	 * @return 첨부파일의 그룹 아이디
	 */
	public String upload(List<MultipartFile> attachFiles) {
		if (attachFiles != null && attachFiles.size() > 0) {

			// 첨부파일 없이 댓글/글 작성할 때 파일 그룹아이디가 생성되는 문제 수정.
			boolean isUploaded = attachFiles.stream().anyMatch(multipartFile -> !multipartFile.isEmpty());
			if (isUploaded) {
				String fileGroupId = this.filesDao.selectNewFileGroupId();
				this.filesDao.insertFileGroupId(fileGroupId);

				this.upload(attachFiles, fileGroupId);
				return fileGroupId;
			}

		}

		return null;
	}

}
