package net.htwater.whale.service;

/**
 * @author wangzhifan
 */
public interface IGenerateContourPictureService {
    /**
     * generateContourPicture
     * @param type duration type
     * @return string
     */
    String generateContourPicture(int type);

    void test(int type);
}
