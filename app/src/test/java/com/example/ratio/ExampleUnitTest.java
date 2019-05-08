package com.example.ratio;

import android.util.Log;

import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Services;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Utilities.DateTransform;
import com.example.ratio.Utilities.FileValidator;
import com.example.ratio.Utilities.TagMaker;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = "ExampleUnitTest";
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void asd() {
        String extension = FilenameUtils.getExtension("joey.exe");
        assertEquals("exe", extension);
    }

    @Test
    public void fileValidatorTest(){
        FileValidator fileValidator = new FileValidator();
        boolean result = fileValidator.isImage("joey.jpeg");
        assertEquals(true, result);
    }

    @Test
    public void toStringTest(){
        Projects projects = new Projects();
        Services services = new Services("SERVICES1", false);
        ProjectType projectType = new ProjectType("PROJECT TYPE", false);
        Subcategory subcategory = new Subcategory("SUBCATEGORY1", false, "DFSFD2");
        projects.setProjectCode("DAS415F");
        projects.setProjectName("RPOJECT NAME 1");
        projects.setProjectOwner("JOEY CARLO");
        projects.setProjectServices(services);
        projects.setProjectType(projectType);
        projects.setProjectSubCategory(subcategory);
        projects.setDeleted(false);

        TagMaker tagMaker = new TagMaker();
        projects.setTags(tagMaker.createTags(projects.toString()));
    }
}