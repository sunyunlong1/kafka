package com.goodstuff.mall.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.goodstuff.mall.core.storage.TencentStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TencentStorageTest {
    @Autowired
    private TencentStorage tencentStorage;

    @Test
    public void test() throws IOException {
        String test = getClass().getClassLoader().getResource("mall.jpg").getFile();
        File testFile = new File(test);
        tencentStorage.store(new FileInputStream(test), testFile.length(), "image/jpg", "mall.jpg");
        Resource resource = tencentStorage.loadAsResource("mall.jpg");
        String url = tencentStorage.generateUrl("mall.jpg");
        System.out.println("test file " + test);
        System.out.println("store file " + resource.getURI());
        System.out.println("generate url " + url);

//        tencentStorage.delete("mall.jpg");
    }

}