package com.example.ratio;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Services;
import com.example.ratio.Entities.Status;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.HelperClasses.FileValidator;
import com.example.ratio.HelperClasses.TagMaker;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        List<Status> projectStatus = new ArrayList<>();
        projectStatus.add(new Status("STATUS1", "1234ABCD"));
        projectStatus.add(new Status("STATUS2", "1234ABCD"));
        List<String> statusString = new ArrayList<>();
        for(Status status : projectStatus){
            statusString.add(status.toString());
        }
        System.out.println(statusString.toString());
        Projects projects = new Projects();
        Services services = new Services("SERVICES1", false);
        ProjectType projectType = new ProjectType("PROJECT TYPE", false);
        Subcategory subcategory = new Subcategory("SUBCATEGORY1", false, "DFSFD2");
        projects.setProjectCode("DAS415F");
        projects.setProjectName("RPOJECT NAME 1");
        projects.setProjectOwner("JOEY CARLO");
        projects.setProjectStatus(projectStatus);
        projects.setProjectServices(services);
        projects.setProjectType(projectType);
        projects.setProjectSubCategory(subcategory);
        projects.setDeleted(false);

        TagMaker tagMaker = new TagMaker();
        projects.setTags(tagMaker.createTags(projects.toString()));
    }
    @Test
    public void project_getBulkTest(){
        DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
        BaseDAO<Projects> projectsBaseDAO = parseFactory.getProjectDAO();
        List<Projects> result = projectsBaseDAO.getBulk(null);
        assertEquals(18, result.size());
    }

    @Test
    public void services_getBulkTest(){
        DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
        BaseDAO<Services> servicesBaseDAO = parseFactory.getServicesDAO();
        Services result = new Services();
        result = servicesBaseDAO.get("2Lm0CIa2mb");
        System.out.println(result.getName());
        //assertEquals(18, result.size());
    }

    @Test
    public void image_GeneratorTest() {
        ImageAPI imageAPI = new ImageAPI();
        System.out.println(imageAPI.generateImage());
        System.out.println(imageAPI.generateImage(1000, 700));

    }
}