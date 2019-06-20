package com.example.ratio.RxJava;

import android.util.Log;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.CustomOperations;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.Entities.Image;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Status;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT;
import com.example.ratio.HelperClasses.RandomImgAPI;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
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
    private GetFromParent<Image> imageGetFromParent = (GetFromParent<Image>) parseFactory.getImageDAO();
    private GetFromParent<Status> statusGetFromParent = (GetFromParent<Status>) parseFactory.getStatusDAO();
    private RandomImgAPI randomImgAPI = new RandomImgAPI();

    public Observable<List<Projects>> getProjectCompleteObservable() {

        Observable<List<Projects>> projectObservable = Observable.defer(() ->
                Observable.just(projectsBaseDAO.getBulk(null)))
                .map(projects -> {
                    List<Projects> projectsList = new ArrayList<>();
                    for (Projects individuals : projects) {
                        List<Status> statusList = statusGetFromParent.getObjects(individuals.getObjectId());
                        individuals.setProjectStatus(statusList);
                        projectsList.add(individuals);
                    }
                    return projectsList;
                })
                .flatMap(projects -> {
                    List<Projects> projectsList = new ArrayList<>();
                    for (Projects individuals : projects) {
                        List<Image> thumbnail = imageGetFromParent.getObjects(individuals.getObjectId());
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
                        List<Status> statusList = statusGetFromParent.getObjects(individuals.getObjectId());
                        individuals.setProjectStatus(statusList);
                        projectsList.add(individuals);
                    }
                    return projectsList;
                })
                .flatMap(projects -> {
                    List<Projects> projectsList = new ArrayList<>();
                    for (Projects individuals : projects) {
                        List<Image> thumbnail = imageGetFromParent.getObjects(individuals.getObjectId());
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

    public Observable<Projects> getProjectFromIDCompleteObservable(String objectId) {
        Observable<Projects> observable = Observable.defer(new Callable<ObservableSource<? extends Projects>>() {
            @Override
            public ObservableSource<? extends Projects> call() throws Exception {
                projectsBaseDAO = parseFactory.getProjectDAO();
                Projects projects = projectsBaseDAO.get(objectId);
                return Observable.just(projects);
            }
        })
                .map(new Function<Projects, Projects>() {
                    @Override
                    public Projects apply(Projects projects) throws Exception {
                        List<Image> imageList = imageGetFromParent.getObjects(projects.getObjectId());
                        if(imageList.size() <= 0 || imageList == null) {
                            Image image = new Image();
                            image.setFilePath(randomImgAPI.generateImage());
                            imageList.add(image);
                        }
                        projects.setThumbnail(imageList.get(0));
                        return projects;
                    }
                })
                .flatMap(new Function<Projects, ObservableSource<Projects>>() {
                    @Override
                    public ObservableSource<Projects> apply(Projects projects) throws Exception {
                        statusGetFromParent = (GetFromParent<Status>) parseFactory.getStatusDAO();
                        List<Status> statuses = statusGetFromParent.getObjects(projects.getObjectId());
                        projects.setProjectStatus(statuses);

                        return Observable.just(projects);
                    }
                });
        return observable;
    }

    public Observable<Projects> uploadProjectConnectable(Projects projects, Image projectThumbnail, List<KeyPairBoolData> projectStatus) {

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
                                for(KeyPairBoolData statuses : projectStatus){
                                    projectStatuses.add(new Status(statuses.getName(), projects.getObjectId()));
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

    public Observable<List<Projects>> getProjectsFromTags(String[] tags) {
        Observable<List<Projects>> observable = Observable.defer(new Callable<ObservableSource<List<Projects>>>() {
            @Override
            public ObservableSource<List<Projects>> call() throws Exception {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT.toString());
                query.whereContainedIn(PROJECT.Tags.toString(), Arrays.asList(tags));
                query.whereEqualTo(PROJECT.DELETED.toString(), false);
                List<Projects> projectsList = projectsCustomOperations.getObject(query);
                Log.d(TAG, "call: Project size: " + projectsList.size());
                return Observable.just(projectsList);
            }
        });
        return observable;
    }

}
