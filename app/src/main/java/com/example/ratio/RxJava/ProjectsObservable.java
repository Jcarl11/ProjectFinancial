package com.example.ratio.RxJava;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.CustomOperations;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Status;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.HelperClasses.RandomImgAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class ProjectsObservable {
    private static final String TAG = "ProjectsObservable";
    private DAOFactory parseFactory = DAOFactory.getDatabase(DATABASES.PARSE);
    private BaseDAO<Projects> projectsBaseDAO = parseFactory.getProjectDAO();
    private CustomOperations<Projects> projectsCustomOperations = (CustomOperations<Projects>) parseFactory.getProjectDAO();
    private BaseDAO<Status> statusBaseDAO = parseFactory.getStatusDAO();
    private BaseDAO<Image> imageBaseDAO = parseFactory.getImageDAO();
    private RandomImgAPI randomImgAPI = new RandomImgAPI();

    public Observable<List<Projects>> getProjectCompleteObservable() {

        Observable<List<Projects>> projectObservable = Observable.defer(() ->
                Observable.just(projectsBaseDAO.getBulk(null)))
                .map(projects -> {
                    List<Projects> projectsList = new ArrayList<>();
                    for (Projects individuals : projects) {
                        List<Status> statusList = statusBaseDAO.getBulk(individuals.getObjectId());
                        individuals.setProjectStatus(statusList);
                        projectsList.add(individuals);
                    }
                    return projectsList;
                })
                .flatMap(projects -> {
                    List<Projects> projectsList = new ArrayList<>();
                    for (Projects individuals : projects) {
                        List<Image> thumbnail = imageBaseDAO.getBulk(individuals.getObjectId());
                        if(thumbnail.size() <= 0 || thumbnail == null) {
                            Image image = new Image();
                            image.setFilePath(randomImgAPI.generateImage());
                            thumbnail.add(image);
                        }
                        individuals.setThumbnail(thumbnail.get(0));
                        projectsList.add(individuals);
                    }
                    return Observable.just(projectsList);
                });
        return projectObservable;
    }
    public Observable<List<Projects>> getProjectSpecificCompleteObservable(String projectCode) {

        Observable<List<Projects>> projectObservable = Observable.defer(() ->
                Observable.just(projectsCustomOperations.getProjectFromCode(projectCode)))
                .map(projects -> {
                    List<Projects> projectsList = new ArrayList<>();
                    for (Projects individuals : projects) {
                        List<Status> statusList = statusBaseDAO.getBulk(individuals.getObjectId());
                        individuals.setProjectStatus(statusList);
                        projectsList.add(individuals);
                    }
                    return projectsList;
                })
                .flatMap(projects -> {
                    List<Projects> projectsList = new ArrayList<>();
                    for (Projects individuals : projects) {
                        List<Image> thumbnail = imageBaseDAO.getBulk(individuals.getObjectId());
                        if(thumbnail.size() <= 0 || thumbnail == null) {
                            Image image = new Image();
                            image.setFilePath(randomImgAPI.generateImage());
                            thumbnail.add(image);
                        }
                        individuals.setThumbnail(thumbnail.get(0));
                        projectsList.add(individuals);
                    }
                    return Observable.just(projectsList);
                });
        return projectObservable;
    }
    public Observable<Projects> uploadProjectConnectable(Projects projects, Image projectThumbnail, List<String> projectStatus) {

        Observable<Projects> deferObs = Observable.defer(new Callable<ObservableSource<Projects>>() {
            @Override
            public ObservableSource<Projects> call() throws Exception {
                return Observable.just(projectsBaseDAO.insert(projects))
                        .map(new Function<Projects, Projects>() {
                            @Override
                            public Projects apply(Projects projects) throws Exception {
                                Log.d(TAG, "apply: Project ObjectID: " + projects.getObjectId() );
                                projectThumbnail.setParent(projects.getObjectId());
                                Image image = imageBaseDAO.insert(projectThumbnail);
                                projects.setThumbnail(image);
                                return projects;
                            }
                        })
                        .map(new Function<Projects, Projects>() {
                            @Override
                            public Projects apply(Projects projects) throws Exception {
                                List<Status> projectStatuses = new ArrayList<>();
                                for(String statuses : projectStatus){
                                    projectStatuses.add(new Status(statuses, projects.getObjectId()));
                                }
                                int result = statusBaseDAO.insertAll(projectStatuses);
                                Log.d(TAG, "apply: Status uploaded: " + result);
                                return projects;
                            }
                        });
            }
        });
        return deferObs;
    }

}
