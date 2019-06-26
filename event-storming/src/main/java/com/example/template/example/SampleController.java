package com.example.template.example;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.*;

@CrossOrigin(origins = "*")
@RestController
public class SampleController {

    public String basicPath = "/Users/kimsanghoon/event/";
    public String projectName = "";

    public String controlTemp;
    public String entityTemp;
    public int controlCount=0;
    public int entityCount=0;


    @RequestMapping(value = "/event/{name}", method = RequestMethod.POST, produces = "application/zip")
    public void call(HttpServletRequest request, HttpServletResponse response,
                     @RequestBody String str, @PathVariable String name)
            throws Exception {
        projectName = name;
        JsonParser jsonParser = new JsonParser();
        JsonObject obj = (JsonObject) jsonParser.parse(str);


        JsonArray elementArray = (JsonArray) obj.get("definition");

        for (int i = 0; i < elementArray.size(); i++) {
            JsonObject jsonobject = (JsonObject) elementArray.get(i);

            if (jsonobject.get("_type").toString().equals("\"org.uengine.uml.model.bounded\"")) {

                JsonArray aggregateArray = (JsonArray) jsonobject.get("dataList");

                for (int j = 0; j < aggregateArray.size(); j++) {
                    JsonObject aggobj = (JsonObject) aggregateArray.get(j); // 어그리케이트 각각의 값 정보

                    //mkProject(바운더리 이름, 어글리게이트 이름)-> 기본 파일 생성
                    mkFolder(jsonobject.get("inputText").toString().replace("\"", ""), aggobj.get("inputText").toString().replace("\"", ""));

                    JsonObject aggregateObj = (JsonObject) aggobj.get("innerAggregate");
                    JsonArray aggregateEntityArr = (JsonArray) aggobj.get("aggregateEntity");

                    if(aggregateEntityArr != null) {
                        for (int zx = 0; zx < aggregateEntityArr.size(); zx++) {
                            JsonObject aggentitiyobj = (JsonObject) aggregateEntityArr.get(zx);
                            System.out.println("("+aggentitiyobj.get("type")+","+aggentitiyobj.get("name")+")");
                            mkUserJavaFile(aggobj.get("inputText").toString().replace("\"", ""),jsonobject.get("inputText").toString().replace("\"", ""),aggentitiyobj.get("type").toString().replace("\"", ""),aggentitiyobj.get("name").toString().replace("\"", ""),aggregateEntityArr.size());
                        }

                    }else {
                        mkUserJavaFile(aggobj.get("inputText").toString().replace("\"", ""),jsonobject.get("inputText").toString().replace("\"", ""),"","",0);

                    }

                    JsonArray domainObj = (JsonArray) aggregateObj.get("domain");

                    for (int z = 0; z < domainObj.size(); z++) {
                        JsonObject ccc = (JsonObject) domainObj.get(z);
                        System.out.println("(" + ccc.get("inputText") + "," + ccc.get("restApi") + ")");

                        String apitype = ccc.get("restApi").toString().replace("\"", "");

                        if (apitype.isEmpty()) {
                            apitype = "GET";
                        }

                        //event별 function 생성
                        mkControlFile(aggobj.get("inputText").toString().replace("\"", ""), jsonobject.get("inputText").toString().replace("\"", ""), ccc.get("inputText").toString().replace("\"", ""), apitype, domainObj.size());
                    }
                    controlCount=0;
                    entityCount=0;
                    controlTemp=null;
                    entityTemp=null;
                }
            }

        }

        //전체 폴더 압
        mkZip();
        System.out.println("Success Zip");

        String filePath = basicPath + projectName + ".zip";
        final int BUFFER_SIZE = 4096;
        ServletContext context = request.getServletContext();
        File downloadFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(downloadFile);
        String mimeType = "application/zip";
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
        response.setHeader("Content-Transfer-Encoding", "binary");
        OutputStream outStream = response.getOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outStream.close();

        deleteFolder(basicPath);
        File fileTmp = new File(basicPath + projectName + ".zip");
        fileTmp.delete();

    }

    @RequestMapping(value = "/event/save/", method = RequestMethod.POST, produces = "application/zip")
    public void save(HttpServletRequest request, HttpServletResponse response,
                     @RequestBody String str, @PathVariable String name)
            throws Exception {

    }

    public static void deleteFolder(String path) {

        File folder = new File(path);
        try {
            if (folder.exists()) {
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기

                for (int i = 0; i < folder_list.length; i++) {
                    if (folder_list[i].isFile()) {
                        folder_list[i].delete();
                    } else {
                        deleteFolder(folder_list[i].getPath()); //재귀함수호출
                    }
                    folder_list[i].delete();
                }
                folder.delete(); //폴더 삭제
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void mkUserJavaFile(String Aggregate,String bound,String type,String name,int size) {
        String path=basicPath+ projectName + "//" + bound;
        try {
            OutputStream userJavaFile = new FileOutputStream(path + "//src//main//java//com//example//template//" + Aggregate + "//" + Aggregate + ".java");

            String userJavaStr ="package com.example.template." + Aggregate + ";\n" +
                    "\n" +
                    "import java.io.Serializable;\n" +
                    "\n" +
                    "import javax.persistence.Entity;\n" +
                    "import javax.persistence.GeneratedValue;\n" +
                    "import javax.persistence.GenerationType;\n" +
                    "import javax.persistence.Id;\n" +
                    "import javax.persistence.Table;\n" +
                    "\n" +
                    "import org.apache.commons.lang3.builder.ToStringBuilder;\n" +
                    "import org.apache.commons.lang3.builder.ToStringStyle;\n" +
                    "\n" +
                    "@Entity\n" +
                    "@Table(name = \"" + Aggregate + "\")\n" +
                    "public class " + Aggregate + " implements Serializable {\n" +
                    "\n" +
                    "   @Id\n" +
                    "   @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                    "   Long id;\n"+
                    "   public Long getId() {\n" +
                    "      return id;\n" +
                    "   }\n" +
                    "\n" +
                    "   public void setId(Long id) {\n" +
                    "      id = id;\n" +
                    "   }";


            if(entityTemp!=null) {
                entityTemp=entityTemp+subMkUserJavaFile(type,name);
            }else {
                entityTemp="";
            }


            userJavaStr=userJavaStr+entityTemp;

            entityCount++;
            if(entityCount == size) {
                userJavaStr=userJavaStr+"\n }";
            }


            byte[] Java=userJavaStr.getBytes();
            userJavaFile.write(Java);

            System.out.println("userJavaFile MAKE");
        }catch(Exception e) {
            e.getStackTrace();
        }
    }

    public String subMkUserJavaFile(String type,String name) {

        String controllfunction =
                "\n\n "+type+" "+name+";\n" +
                        "   public "+type+" get"+name+"() {\n" +
                        "      return "+name+";\n" +
                        "   }\n" +
                        "\n" +
                        "   public void set"+name+"("+type+" "+name+") {\n" +
                        "      this."+name+" = "+name+";\n" +
                        "   }" ;

        return controllfunction;

    }


    public void mkFolder(String bound, String Aggregate) throws IOException {
        if (projectName.isEmpty()) {
            projectName = "undefined";
        }
        String path = basicPath + projectName;
        Path mainPath = Paths.get(path + "//" + bound + "//src//main//java//com//example//template//config");
        Path subPath = Paths.get(path + "//" + bound + "//src//main//java//com//example//template//" + Aggregate);
        Path resourcesPath = Paths.get(path + "//" + bound + "//src//main//resources");

        Files.createDirectories(mainPath); //폴더 생성합니다.
        Files.createDirectories(subPath); //폴더 생성합니다.
        Files.createDirectories(resourcesPath); //폴더 생성합니다.

        //전체적인 기본 파일 생성
        mkBasicFile(bound, Aggregate);

    }

    public void mkZip() throws IOException {
        File dir = new File(basicPath + projectName);
        File file = null;
        String files[] = null;

        //파일이 디렉토리 일경우 리스트를 읽어오고
        //파일이 디렉토리가 아니면 첫번째 배열에 파일이름을 넣는다.
        if (dir.isDirectory()) {
            files = dir.list();
        } else {
            files = new String[1];
            files[0] = dir.getName();
        }

        ZipArchiveOutputStream zos = null;

        try {
            // Zip 파일생성
            zos = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(basicPath + projectName + ".zip")));

            for (int i = 0; i < files.length; i++) {
                file = new File(basicPath + projectName + "//" + files[i]);
                zip("", file, zos, basicPath + projectName);

            }
            zos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                zos.close();
            }
        }

    }


    public void mkControlFile(String Aggregate,String bound,String event,String APItype,int size) {

        try {
            OutputStream controllJavaFile = new FileOutputStream(basicPath+projectName+"//"+bound+"//src//main//java//com//example//template//"+Aggregate+"//"+Aggregate+"Controller.java");

            String controllJavaStr ="package com.example.template."+Aggregate+";\n" +
                    "\n" +
                    "import javax.servlet.http.HttpServletRequest;\n" +
                    "import javax.servlet.http.HttpServletResponse;\n" +
                    "\n" +
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.web.bind.annotation.PathVariable;\n" +
                    "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                    "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                    "import org.springframework.web.bind.annotation.RestController;\n\n"+
                    "@CrossOrigin(origins=\"*\")\n" +
                    "@RestController\n" +
                    "@RequestMapping(\"/"+Aggregate +"\")\n" +
                    "public class "+Aggregate+"Controller {\n";

            if(controlTemp==null) {
                controlTemp=subMkControlFile(Aggregate,event,APItype);

            }else {
                controlTemp=controlTemp+subMkControlFile(Aggregate,event,APItype);

            }

            controllJavaStr=controllJavaStr+controlTemp;

            controlCount++;
            if(controlCount == size) {
                controllJavaStr=controllJavaStr+"}";
            }


            byte[] controll=controllJavaStr.getBytes();
            controllJavaFile.write(controll);

            System.out.println("controllJava MAKE");
        }catch(Exception e) {
            e.getStackTrace();
        }
    }

    public String subMkControlFile(String Aggregate,String event,String APItype) {

        String controllfunction =
                "\n" +
                        "    @RequestMapping(value = \"/"+event+"\", method = RequestMethod."+APItype+", produces = \"application/json;charset=UTF-8\")\n" +
                        "   public "+Aggregate+" "+event+" (HttpServletRequest request, HttpServletResponse response) \n" +
                        "         throws Exception {\n" +
                        "          return null;\n" +
                        "           }\n" +
                        "   \n" +
                        "\n" ;

        return controllfunction;

    }


    //압축파일에 파일작성
    public static void zip(String parent, File file, ZipArchiveOutputStream zos, String rootPath) throws
            IOException {

        FileInputStream fis = null;
        BufferedInputStream bis = null;

        //buffer size
        int size = 1024;
        byte[] buf = new byte[size];

        if (!file.exists()) {
            System.out.println(file.getName() + " : 파일없음");
        }

        //해당 폴더안에 다른 폴더가 재귀호출
        if (file.isDirectory()) {

            String dirName = file.getPath().replace(rootPath, "");
            String parentName = dirName.substring(1) + "/";

            dirName = dirName.substring(1, dirName.length() - file.getName().length());
            ZipArchiveEntry entry = new ZipArchiveEntry(dirName + file.getName() + "/");
            zos.putArchiveEntry(entry);
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                zip(parentName, new File(file.getPath() + "/" + files[i]), zos, rootPath);
            }

        } else {
            //encoding 설정
            zos.setEncoding("UTF-8");

            //buffer에 해당파일의 stream을 입력한다.
            //System.out.println(file.getPath());
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis, size);

            //zip에 넣을 다음 entry 를 가져온다.
            ZipArchiveEntry entry = new ZipArchiveEntry(parent + file.getName());
            zos.putArchiveEntry(entry);


            //준비된 버퍼에서 집출력스트림으로 write 한다.
            int len;
            while ((len = bis.read(buf, 0, size)) != -1) {
                zos.write(buf, 0, len);
            }

            bis.close();
            fis.close();
            zos.closeArchiveEntry();
        }
    }


    public void mkBasicFile(String bound, String Aggregate) {
        String path = basicPath + projectName + "//" + bound;
        try {
            OutputStream DockerfileFile = new FileOutputStream(path + "//Dockerfile");
            OutputStream cloudbuildFile = new FileOutputStream(path + "//cloudbuild-dev.yaml");
            OutputStream mvnwFile = new FileOutputStream(path + "//mvnw");
            OutputStream mvnwcmdFile = new FileOutputStream(path + "//mvnw.cmd");
            OutputStream pomFile = new FileOutputStream(path + "//pom.xml");

            OutputStream applicationFile = new FileOutputStream(path + "//src//main//resources//application.yml");

            OutputStream applicationJavaFile = new FileOutputStream(path + "//src//main//java//com//example//template//Application.java");

            OutputStream serviceJavaFile = new FileOutputStream(path + "//src//main//java//com//example//template//" + Aggregate + "//" + Aggregate + "Service.java");
            OutputStream userJavaFile = new FileOutputStream(path + "//src//main//java//com//example//template//" + Aggregate + "//" + Aggregate + ".java");
            OutputStream repositoryJavaFile = new FileOutputStream(path + "//src//main//java//com//example//template//" + Aggregate + "//" + Aggregate + "Repository.java");


            String DockerStr = "FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN\n" +
                    "COPY pom.xml /tmp/\n" +
                    "COPY src /tmp/src/\n" +
                    "WORKDIR /tmp/\n" +
                    "RUN mvn package install\n" +
                    "\n" +
                    "FROM openjdk:8u111-jdk-alpine\n" +
                    "COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar app.jar\n" +
                    "EXPOSE 8080\n" +
                    "ENTRYPOINT [\"java\",\"-Xmx400M\",\"-Djava.security.egd=file:/dev/./urandom\",\"-jar\",\"/app.jar\",\"--spring.profiles.active=docker\"]";


            String cloudbuildStr = "steps:\n" +
                    "  ### Build\n" +
                    "  - id: 'build'\n" +
                    "    name: 'gcr.io/cloud-builders/docker'\n" +
                    "    entrypoint: 'bash'\n" +
                    "    args:\n" +
                    "      - '-c'\n" +
                    "      - |\n" +
                    "        echo '$COMMIT_SHA =' $COMMIT_SHA\n" +
                    "        docker build -t gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA .\n" +
                    "  ### Test\n" +
                    "  ### Publish\n" +
                    "  - id: 'publish'\n" +
                    "    name: 'gcr.io/cloud-builders/docker'\n" +
                    "    entrypoint: 'bash'\n" +
                    "    args:\n" +
                    "      - '-c'\n" +
                    "      - |\n" +
                    "        docker push gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA\n" +
                    "  ### deploy\n" +
                    "  - id: 'deploy'\n" +
                    "    name: 'gcr.io/cloud-builders/gcloud'\n" +
                    "    env:\n" +
                    "      - 'CLOUDSDK_COMPUTE_ZONE=asia-northeast1-a'\n" +
                    "      - 'CLOUDSDK_CONTAINER_CLUSTER=standard-cluster-1'\n" +
                    "      - 'KUBECONFIG=/kube/config'\n" +
                    "    entrypoint: 'bash'\n" +
                    "    args:\n" +
                    "      - '-c'\n" +
                    "      - |\n" +
                    "        CLUSTER=standard-cluster-1\n" +
                    "        PROJECT=$$(gcloud config get-value core/project)\n" +
                    "        ZONE=asia-northeast1-a\n" +
                    "        gcloud container clusters get-credentials \"$${CLUSTER}\" \\\n" +
                    "          --project \"$${PROJECT}\" \\\n" +
                    "          --zone \"$${ZONE}\"\n" +
                    "        cat <<EOF | kubectl apply -f -\n" +
                    "        apiVersion: apps/v1\n" +
                    "        kind: Deployment\n" +
                    "        metadata:\n" +
                    "          name: $_PROJECT_NAME\n" +
                    "          labels:\n" +
                    "            app: $_PROJECT_NAME\n" +
                    "        spec:\n" +
                    "          replicas: 2\n" +
                    "          selector:\n" +
                    "            matchLabels:\n" +
                    "              app: $_PROJECT_NAME\n" +
                    "          template:\n" +
                    "            metadata:\n" +
                    "              labels:\n" +
                    "                app: $_PROJECT_NAME\n" +
                    "            spec:\n" +
                    "              containers:\n" +
                    "                - name: $_PROJECT_NAME\n" +
                    "                  image: gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA\n" +
                    "                  ports:\n" +
                    "                    - containerPort: 8080\n" +
                    "        EOF\n" +
                    "#  - id: 'deploy2'\n" +
                    "#    name: 'gcr.io/cloud-builders/kubectl'\n" +
                    "#    args:\n" +
                    "#      - set\n" +
                    "#      - image\n" +
                    "#      - deployment\n" +
                    "#      - $_PROJECT_NAME\n" +
                    "#      - delivery=gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA\n" +
                    "#    env:\n" +
                    "#      - 'CLOUDSDK_COMPUTE_ZONE=asia-northeast1-a'\n" +
                    "#      - 'CLOUDSDK_CONTAINER_CLUSTER=standard-cluster-1'";


            String mvnwStr = "#!/bin/sh\n" +
                    "# ----------------------------------------------------------------------------\n" +
                    "# Licensed to the Apache Software Foundation (ASF) under one\n" +
                    "# or more contributor license agreements.  See the NOTICE file\n" +
                    "# distributed with this work for additional information\n" +
                    "# regarding copyright ownership.  The ASF licenses this file\n" +
                    "# to you under the Apache License, Version 2.0 (the\n" +
                    "# \"License\"); you may not use this file except in compliance\n" +
                    "# with the License.  You may obtain a copy of the License at\n" +
                    "#\n" +
                    "#    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "#\n" +
                    "# Unless required by applicable law or agreed to in writing,\n" +
                    "# software distributed under the License is distributed on an\n" +
                    "# \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n" +
                    "# KIND, either express or implied.  See the License for the\n" +
                    "# specific language governing permissions and limitations\n" +
                    "# under the License.\n" +
                    "# ----------------------------------------------------------------------------\n" +
                    "\n" +
                    "# ----------------------------------------------------------------------------\n" +
                    "# Maven2 Start Up Batch script\n" +
                    "#\n" +
                    "# Required ENV vars:\n" +
                    "# ------------------\n" +
                    "#   JAVA_HOME - location of a JDK home dir\n" +
                    "#\n" +
                    "# Optional ENV vars\n" +
                    "# -----------------\n" +
                    "#   M2_HOME - location of maven2's installed home dir\n" +
                    "#   MAVEN_OPTS - parameters passed to the Java VM when running Maven\n" +
                    "#     e.g. to debug Maven itself, use\n" +
                    "#       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000\n" +
                    "#   MAVEN_SKIP_RC - flag to disable loading of mavenrc files\n" +
                    "# ----------------------------------------------------------------------------\n" +
                    "\n" +
                    "if [ -z \"$MAVEN_SKIP_RC\" ] ; then\n" +
                    "\n" +
                    "  if [ -f /etc/mavenrc ] ; then\n" +
                    "    . /etc/mavenrc\n" +
                    "  fi\n" +
                    "\n" +
                    "  if [ -f \"$HOME/.mavenrc\" ] ; then\n" +
                    "    . \"$HOME/.mavenrc\"\n" +
                    "  fi\n" +
                    "\n" +
                    "fi\n" +
                    "\n" +
                    "# OS specific support.  $var _must_ be set to either true or false.\n" +
                    "cygwin=false;\n" +
                    "darwin=false;\n" +
                    "mingw=false\n" +
                    "case \"`uname`\" in\n" +
                    "  CYGWIN*) cygwin=true ;;\n" +
                    "  MINGW*) mingw=true;;\n" +
                    "  Darwin*) darwin=true\n" +
                    "    # Use /usr/libexec/java_home if available, otherwise fall back to /Library/Java/Home\n" +
                    "    # See https://developer.apple.com/library/mac/qa/qa1170/_index.html\n" +
                    "    if [ -z \"$JAVA_HOME\" ]; then\n" +
                    "      if [ -x \"/usr/libexec/java_home\" ]; then\n" +
                    "        export JAVA_HOME=\"`/usr/libexec/java_home`\"\n" +
                    "      else\n" +
                    "        export JAVA_HOME=\"/Library/Java/Home\"\n" +
                    "      fi\n" +
                    "    fi\n" +
                    "    ;;\n" +
                    "esac\n" +
                    "\n" +
                    "if [ -z \"$JAVA_HOME\" ] ; then\n" +
                    "  if [ -r /etc/gentoo-release ] ; then\n" +
                    "    JAVA_HOME=`java-config --jre-home`\n" +
                    "  fi\n" +
                    "fi\n" +
                    "\n" +
                    "if [ -z \"$M2_HOME\" ] ; then\n" +
                    "  ## resolve links - $0 may be a link to maven's home\n" +
                    "  PRG=\"$0\"\n" +
                    "\n" +
                    "  # need this for relative symlinks\n" +
                    "  while [ -h \"$PRG\" ] ; do\n" +
                    "    ls=`ls -ld \"$PRG\"`\n" +
                    "    link=`expr \"$ls\" : '.*-> \\(.*\\)$'`\n" +
                    "    if expr \"$link\" : '/.*' > /dev/null; then\n" +
                    "      PRG=\"$link\"\n" +
                    "    else\n" +
                    "      PRG=\"`dirname \"$PRG\"`/$link\"\n" +
                    "    fi\n" +
                    "  done\n" +
                    "\n" +
                    "  saveddir=`pwd`\n" +
                    "\n" +
                    "  M2_HOME=`dirname \"$PRG\"`/..\n" +
                    "\n" +
                    "  # make it fully qualified\n" +
                    "  M2_HOME=`cd \"$M2_HOME\" && pwd`\n" +
                    "\n" +
                    "  cd \"$saveddir\"\n" +
                    "  # echo Using m2 at $M2_HOME\n" +
                    "fi\n" +
                    "\n" +
                    "# For Cygwin, ensure paths are in UNIX format before anything is touched\n" +
                    "if $cygwin ; then\n" +
                    "  [ -n \"$M2_HOME\" ] &&\n" +
                    "    M2_HOME=`cygpath --unix \"$M2_HOME\"`\n" +
                    "  [ -n \"$JAVA_HOME\" ] &&\n" +
                    "    JAVA_HOME=`cygpath --unix \"$JAVA_HOME\"`\n" +
                    "  [ -n \"$CLASSPATH\" ] &&\n" +
                    "    CLASSPATH=`cygpath --path --unix \"$CLASSPATH\"`\n" +
                    "fi\n" +
                    "\n" +
                    "# For Mingw, ensure paths are in UNIX format before anything is touched\n" +
                    "if $mingw ; then\n" +
                    "  [ -n \"$M2_HOME\" ] &&\n" +
                    "    M2_HOME=\"`(cd \"$M2_HOME\"; pwd)`\"\n" +
                    "  [ -n \"$JAVA_HOME\" ] &&\n" +
                    "    JAVA_HOME=\"`(cd \"$JAVA_HOME\"; pwd)`\"\n" +
                    "  # TODO classpath?\n" +
                    "fi\n" +
                    "\n" +
                    "if [ -z \"$JAVA_HOME\" ]; then\n" +
                    "  javaExecutable=\"`which javac`\"\n" +
                    "  if [ -n \"$javaExecutable\" ] && ! [ \"`expr \\\"$javaExecutable\\\" : '\\([^ ]*\\)'`\" = \"no\" ]; then\n" +
                    "    # readlink(1) is not available as standard on Solaris 10.\n" +
                    "    readLink=`which readlink`\n" +
                    "    if [ ! `expr \"$readLink\" : '\\([^ ]*\\)'` = \"no\" ]; then\n" +
                    "      if $darwin ; then\n" +
                    "        javaHome=\"`dirname \\\"$javaExecutable\\\"`\"\n" +
                    "        javaExecutable=\"`cd \\\"$javaHome\\\" && pwd -P`/javac\"\n" +
                    "      else\n" +
                    "        javaExecutable=\"`readlink -f \\\"$javaExecutable\\\"`\"\n" +
                    "      fi\n" +
                    "      javaHome=\"`dirname \\\"$javaExecutable\\\"`\"\n" +
                    "      javaHome=`expr \"$javaHome\" : '\\(.*\\)/bin'`\n" +
                    "      JAVA_HOME=\"$javaHome\"\n" +
                    "      export JAVA_HOME\n" +
                    "    fi\n" +
                    "  fi\n" +
                    "fi\n" +
                    "\n" +
                    "if [ -z \"$JAVACMD\" ] ; then\n" +
                    "  if [ -n \"$JAVA_HOME\"  ] ; then\n" +
                    "    if [ -x \"$JAVA_HOME/jre/sh/java\" ] ; then\n" +
                    "      # IBM's JDK on AIX uses strange locations for the executables\n" +
                    "      JAVACMD=\"$JAVA_HOME/jre/sh/java\"\n" +
                    "    else\n" +
                    "      JAVACMD=\"$JAVA_HOME/bin/java\"\n" +
                    "    fi\n" +
                    "  else\n" +
                    "    JAVACMD=\"`which java`\"\n" +
                    "  fi\n" +
                    "fi\n" +
                    "\n" +
                    "if [ ! -x \"$JAVACMD\" ] ; then\n" +
                    "  echo \"Error: JAVA_HOME is not defined correctly.\" >&2\n" +
                    "  echo \"  We cannot execute $JAVACMD\" >&2\n" +
                    "  exit 1\n" +
                    "fi\n" +
                    "\n" +
                    "if [ -z \"$JAVA_HOME\" ] ; then\n" +
                    "  echo \"Warning: JAVA_HOME environment variable is not set.\"\n" +
                    "fi\n" +
                    "\n" +
                    "CLASSWORLDS_LAUNCHER=org.codehaus.plexus.classworlds.launcher.Launcher\n" +
                    "\n" +
                    "# traverses directory structure from process work directory to filesystem root\n" +
                    "# first directory with .mvn subdirectory is considered project base directory\n" +
                    "find_maven_basedir() {\n" +
                    "\n" +
                    "  if [ -z \"$1\" ]\n" +
                    "  then\n" +
                    "    echo \"Path not specified to find_maven_basedir\"\n" +
                    "    return 1\n" +
                    "  fi\n" +
                    "\n" +
                    "  basedir=\"$1\"\n" +
                    "  wdir=\"$1\"\n" +
                    "  while [ \"$wdir\" != '/' ] ; do\n" +
                    "    if [ -d \"$wdir\"/.mvn ] ; then\n" +
                    "      basedir=$wdir\n" +
                    "      break\n" +
                    "    fi\n" +
                    "    # workaround for JBEAP-8937 (on Solaris 10/Sparc)\n" +
                    "    if [ -d \"${wdir}\" ]; then\n" +
                    "      wdir=`cd \"$wdir/..\"; pwd`\n" +
                    "    fi\n" +
                    "    # end of workaround\n" +
                    "  done\n" +
                    "  echo \"${basedir}\"\n" +
                    "}\n" +
                    "\n" +
                    "# concatenates all lines of a file\n" +
                    "concat_lines() {\n" +
                    "  if [ -f \"$1\" ]; then\n" +
                    "    echo \"$(tr -s '\\n' ' ' < \"$1\")\"\n" +
                    "  fi\n" +
                    "}\n" +
                    "\n" +
                    "BASE_DIR=`find_maven_basedir \"$(pwd)\"`\n" +
                    "if [ -z \"$BASE_DIR\" ]; then\n" +
                    "  exit 1;\n" +
                    "fi\n" +
                    "\n" +
                    "##########################################################################################\n" +
                    "# Extension to allow automatically downloading the maven-wrapper.jar from Maven-central\n" +
                    "# This allows using the maven wrapper in projects that prohibit checking in binary data.\n" +
                    "##########################################################################################\n" +
                    "if [ -r \"$BASE_DIR/.mvn/wrapper/maven-wrapper.jar\" ]; then\n" +
                    "    if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                    "      echo \"Found .mvn/wrapper/maven-wrapper.jar\"\n" +
                    "    fi\n" +
                    "else\n" +
                    "    if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                    "      echo \"Couldn't find .mvn/wrapper/maven-wrapper.jar, downloading it ...\"\n" +
                    "    fi\n" +
                    "    jarUrl=\"https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.2/maven-wrapper-0.4.2.jar\"\n" +
                    "    while IFS=\"=\" read key value; do\n" +
                    "      case \"$key\" in (wrapperUrl) jarUrl=\"$value\"; break ;;\n" +
                    "      esac\n" +
                    "    done < \"$BASE_DIR/.mvn/wrapper/maven-wrapper.properties\"\n" +
                    "    if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                    "      echo \"Downloading from: $jarUrl\"\n" +
                    "    fi\n" +
                    "    wrapperJarPath=\"$BASE_DIR/.mvn/wrapper/maven-wrapper.jar\"\n" +
                    "\n" +
                    "    if command -v wget > /dev/null; then\n" +
                    "        if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                    "          echo \"Found wget ... using wget\"\n" +
                    "        fi\n" +
                    "        wget \"$jarUrl\" -O \"$wrapperJarPath\"\n" +
                    "    elif command -v curl > /dev/null; then\n" +
                    "        if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                    "          echo \"Found curl ... using curl\"\n" +
                    "        fi\n" +
                    "        curl -o \"$wrapperJarPath\" \"$jarUrl\"\n" +
                    "    else\n" +
                    "        if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                    "          echo \"Falling back to using Java to download\"\n" +
                    "        fi\n" +
                    "        javaClass=\"$BASE_DIR/.mvn/wrapper/MavenWrapperDownloader.java\"\n" +
                    "        if [ -e \"$javaClass\" ]; then\n" +
                    "            if [ ! -e \"$BASE_DIR/.mvn/wrapper/MavenWrapperDownloader.class\" ]; then\n" +
                    "                if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                    "                  echo \" - Compiling MavenWrapperDownloader.java ...\"\n" +
                    "                fi\n" +
                    "                # Compiling the Java class\n" +
                    "                (\"$JAVA_HOME/bin/javac\" \"$javaClass\")\n" +
                    "            fi\n" +
                    "            if [ -e \"$BASE_DIR/.mvn/wrapper/MavenWrapperDownloader.class\" ]; then\n" +
                    "                # Running the downloader\n" +
                    "                if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                    "                  echo \" - Running MavenWrapperDownloader.java ...\"\n" +
                    "                fi\n" +
                    "                (\"$JAVA_HOME/bin/java\" -cp .mvn/wrapper MavenWrapperDownloader \"$MAVEN_PROJECTBASEDIR\")\n" +
                    "            fi\n" +
                    "        fi\n" +
                    "    fi\n" +
                    "fi\n" +
                    "##########################################################################################\n" +
                    "# End of extension\n" +
                    "##########################################################################################\n" +
                    "\n" +
                    "export MAVEN_PROJECTBASEDIR=${MAVEN_BASEDIR:-\"$BASE_DIR\"}\n" +
                    "if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                    "  echo $MAVEN_PROJECTBASEDIR\n" +
                    "fi\n" +
                    "MAVEN_OPTS=\"$(concat_lines \"$MAVEN_PROJECTBASEDIR/.mvn/jvm.config\") $MAVEN_OPTS\"\n" +
                    "\n" +
                    "# For Cygwin, switch paths to Windows format before running java\n" +
                    "if $cygwin; then\n" +
                    "  [ -n \"$M2_HOME\" ] &&\n" +
                    "    M2_HOME=`cygpath --path --windows \"$M2_HOME\"`\n" +
                    "  [ -n \"$JAVA_HOME\" ] &&\n" +
                    "    JAVA_HOME=`cygpath --path --windows \"$JAVA_HOME\"`\n" +
                    "  [ -n \"$CLASSPATH\" ] &&\n" +
                    "    CLASSPATH=`cygpath --path --windows \"$CLASSPATH\"`\n" +
                    "  [ -n \"$MAVEN_PROJECTBASEDIR\" ] &&\n" +
                    "    MAVEN_PROJECTBASEDIR=`cygpath --path --windows \"$MAVEN_PROJECTBASEDIR\"`\n" +
                    "fi\n" +
                    "\n" +
                    "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain\n" +
                    "\n" +
                    "exec \"$JAVACMD\" \\\n" +
                    "  $MAVEN_OPTS \\\n" +
                    "  -classpath \"$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar\" \\\n" +
                    "  \"-Dmaven.home=${M2_HOME}\" \"-Dmaven.multiModuleProjectDirectory=${MAVEN_PROJECTBASEDIR}\" \\\n" +
                    "  ${WRAPPER_LAUNCHER} $MAVEN_CONFIG \"$@\"";


            String mvnwcmdStr = "@REM ----------------------------------------------------------------------------\n" +
                    "@REM Licensed to the Apache Software Foundation (ASF) under one\n" +
                    "@REM or more contributor license agreements.  See the NOTICE file\n" +
                    "@REM distributed with this work for additional information\n" +
                    "@REM regarding copyright ownership.  The ASF licenses this file\n" +
                    "@REM to you under the Apache License, Version 2.0 (the\n" +
                    "@REM \"License\"); you may not use this file except in compliance\n" +
                    "@REM with the License.  You may obtain a copy of the License at\n" +
                    "@REM\n" +
                    "@REM    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "@REM\n" +
                    "@REM Unless required by applicable law or agreed to in writing,\n" +
                    "@REM software distributed under the License is distributed on an\n" +
                    "@REM \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n" +
                    "@REM KIND, either express or implied.  See the License for the\n" +
                    "@REM specific language governing permissions and limitations\n" +
                    "@REM under the License.\n" +
                    "@REM ----------------------------------------------------------------------------\n" +
                    "\n" +
                    "@REM ----------------------------------------------------------------------------\n" +
                    "@REM Maven2 Start Up Batch script\n" +
                    "@REM\n" +
                    "@REM Required ENV vars:\n" +
                    "@REM JAVA_HOME - location of a JDK home dir\n" +
                    "@REM\n" +
                    "@REM Optional ENV vars\n" +
                    "@REM M2_HOME - location of maven2's installed home dir\n" +
                    "@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands\n" +
                    "@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending\n" +
                    "@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven\n" +
                    "@REM     e.g. to debug Maven itself, use\n" +
                    "@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000\n" +
                    "@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files\n" +
                    "@REM ----------------------------------------------------------------------------\n" +
                    "\n" +
                    "@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'\n" +
                    "@echo off\n" +
                    "@REM set title of command window\n" +
                    "title %0\n" +
                    "@REM enable echoing my setting MAVEN_BATCH_ECHO to 'on'\n" +
                    "@if \"%MAVEN_BATCH_ECHO%\" == \"on\"  echo %MAVEN_BATCH_ECHO%\n" +
                    "\n" +
                    "@REM set %HOME% to equivalent of $HOME\n" +
                    "if \"%HOME%\" == \"\" (set \"HOME=%HOMEDRIVE%%HOMEPATH%\")\n" +
                    "\n" +
                    "@REM Execute a user defined script before this one\n" +
                    "if not \"%MAVEN_SKIP_RC%\" == \"\" goto skipRcPre\n" +
                    "@REM check for pre script, once with legacy .bat ending and once with .cmd ending\n" +
                    "if exist \"%HOME%\\mavenrc_pre.bat\" call \"%HOME%\\mavenrc_pre.bat\"\n" +
                    "if exist \"%HOME%\\mavenrc_pre.cmd\" call \"%HOME%\\mavenrc_pre.cmd\"\n" +
                    ":skipRcPre\n" +
                    "\n" +
                    "@setlocal\n" +
                    "\n" +
                    "set ERROR_CODE=0\n" +
                    "\n" +
                    "@REM To isolate internal variables from possible post scripts, we use another setlocal\n" +
                    "@setlocal\n" +
                    "\n" +
                    "@REM ==== START VALIDATION ====\n" +
                    "if not \"%JAVA_HOME%\" == \"\" goto OkJHome\n" +
                    "\n" +
                    "echo.\n" +
                    "echo Error: JAVA_HOME not found in your environment. >&2\n" +
                    "echo Please set the JAVA_HOME variable in your environment to match the >&2\n" +
                    "echo location of your Java installation. >&2\n" +
                    "echo.\n" +
                    "goto error\n" +
                    "\n" +
                    ":OkJHome\n" +
                    "if exist \"%JAVA_HOME%\\bin\\java.exe\" goto init\n" +
                    "\n" +
                    "echo.\n" +
                    "echo Error: JAVA_HOME is set to an invalid directory. >&2\n" +
                    "echo JAVA_HOME = \"%JAVA_HOME%\" >&2\n" +
                    "echo Please set the JAVA_HOME variable in your environment to match the >&2\n" +
                    "echo location of your Java installation. >&2\n" +
                    "echo.\n" +
                    "goto error\n" +
                    "\n" +
                    "@REM ==== END VALIDATION ====\n" +
                    "\n" +
                    ":init\n" +
                    "\n" +
                    "@REM Find the project base dir, i.e. the directory that contains the folder \".mvn\".\n" +
                    "@REM Fallback to current working directory if not found.\n" +
                    "\n" +
                    "set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%\n" +
                    "IF NOT \"%MAVEN_PROJECTBASEDIR%\"==\"\" goto endDetectBaseDir\n" +
                    "\n" +
                    "set EXEC_DIR=%CD%\n" +
                    "set WDIR=%EXEC_DIR%\n" +
                    ":findBaseDir\n" +
                    "IF EXIST \"%WDIR%\"\\.mvn goto baseDirFound\n" +
                    "cd ..\n" +
                    "IF \"%WDIR%\"==\"%CD%\" goto baseDirNotFound\n" +
                    "set WDIR=%CD%\n" +
                    "goto findBaseDir\n" +
                    "\n" +
                    ":baseDirFound\n" +
                    "set MAVEN_PROJECTBASEDIR=%WDIR%\n" +
                    "cd \"%EXEC_DIR%\"\n" +
                    "goto endDetectBaseDir\n" +
                    "\n" +
                    ":baseDirNotFound\n" +
                    "set MAVEN_PROJECTBASEDIR=%EXEC_DIR%\n" +
                    "cd \"%EXEC_DIR%\"\n" +
                    "\n" +
                    ":endDetectBaseDir\n" +
                    "\n" +
                    "IF NOT EXIST \"%MAVEN_PROJECTBASEDIR%\\.mvn\\jvm.config\" goto endReadAdditionalConfig\n" +
                    "\n" +
                    "@setlocal EnableExtensions EnableDelayedExpansion\n" +
                    "for /F \"usebackq delims=\" %%a in (\"%MAVEN_PROJECTBASEDIR%\\.mvn\\jvm.config\") do set JVM_CONFIG_MAVEN_PROPS=!JVM_CONFIG_MAVEN_PROPS! %%a\n" +
                    "@endlocal & set JVM_CONFIG_MAVEN_PROPS=%JVM_CONFIG_MAVEN_PROPS%\n" +
                    "\n" +
                    ":endReadAdditionalConfig\n" +
                    "\n" +
                    "SET MAVEN_JAVA_EXE=\"%JAVA_HOME%\\bin\\java.exe\"\n" +
                    "set WRAPPER_JAR=\"%MAVEN_PROJECTBASEDIR%\\.mvn\\wrapper\\maven-wrapper.jar\"\n" +
                    "set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain\n" +
                    "\n" +
                    "set DOWNLOAD_URL=\"https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.2/maven-wrapper-0.4.2.jar\"\n" +
                    "FOR /F \"tokens=1,2 delims==\" %%A IN (%MAVEN_PROJECTBASEDIR%\\.mvn\\wrapper\\maven-wrapper.properties) DO (\n" +
                    "   IF \"%%A\"==\"wrapperUrl\" SET DOWNLOAD_URL=%%B \n" +
                    ")\n" +
                    "\n" +
                    "@REM Extension to allow automatically downloading the maven-wrapper.jar from Maven-central\n" +
                    "@REM This allows using the maven wrapper in projects that prohibit checking in binary data.\n" +
                    "if exist %WRAPPER_JAR% (\n" +
                    "    echo Found %WRAPPER_JAR%\n" +
                    ") else (\n" +
                    "    echo Couldn't find %WRAPPER_JAR%, downloading it ...\n" +
                    "   echo Downloading from: %DOWNLOAD_URL%\n" +
                    "    powershell -Command \"(New-Object Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')\"\n" +
                    "    echo Finished downloading %WRAPPER_JAR%\n" +
                    ")\n" +
                    "@REM End of extension\n" +
                    "\n" +
                    "%MAVEN_JAVA_EXE% %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath %WRAPPER_JAR% \"-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%\" %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*\n" +
                    "if ERRORLEVEL 1 goto error\n" +
                    "goto end\n" +
                    "\n" +
                    ":error\n" +
                    "set ERROR_CODE=1\n" +
                    "\n" +
                    ":end\n" +
                    "@endlocal & set ERROR_CODE=%ERROR_CODE%\n" +
                    "\n" +
                    "if not \"%MAVEN_SKIP_RC%\" == \"\" goto skipRcPost\n" +
                    "@REM check for post script, once with legacy .bat ending and once with .cmd ending\n" +
                    "if exist \"%HOME%\\mavenrc_post.bat\" call \"%HOME%\\mavenrc_post.bat\"\n" +
                    "if exist \"%HOME%\\mavenrc_post.cmd\" call \"%HOME%\\mavenrc_post.cmd\"\n" +
                    ":skipRcPost\n" +
                    "\n" +
                    "@REM pause the script if MAVEN_BATCH_PAUSE is set to 'on'\n" +
                    "if \"%MAVEN_BATCH_PAUSE%\" == \"on\" pause\n" +
                    "\n" +
                    "if \"%MAVEN_TERMINATE_CMD%\" == \"on\" exit %ERROR_CODE%\n" +
                    "\n" +
                    "exit /B %ERROR_CODE%";


            String pomStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "   xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                    "   <modelVersion>4.0.0</modelVersion>\n" +
                    "   <parent>\n" +
                    "      <groupId>org.springframework.boot</groupId>\n" +
                    "      <artifactId>spring-boot-starter-parent</artifactId>\n" +
                    "      <version>2.1.1.RELEASE</version>\n" +
                    "      <relativePath/> <!-- lookup parent from repository -->\n" +
                    "   </parent>\n" +
                    "   <groupId>com.example</groupId>\n" +
                    "   <artifactId>boot-camp-template</artifactId>\n" +
                    "   <version>0.0.1-SNAPSHOT</version>\n" +
                    "   <name>boot-camp-delivery</name>\n" +
                    "   <description>Demo project for Spring Boot</description>\n" +
                    "\n" +
                    "   <properties>\n" +
                    "      <java.version>1.8</java.version>\n" +
                    "      <spring-cloud.version>Greenwich.RELEASE</spring-cloud.version>\n" +
                    "   </properties>\n" +
                    "\n" +
                    "   <dependencies>\n" +
                    "      <dependency>\n" +
                    "         <groupId>org.springframework.boot</groupId>\n" +
                    "         <artifactId>spring-boot-starter-data-rest</artifactId>\n" +
                    "      </dependency>\n" +
                    "      <dependency>\n" +
                    "         <groupId>org.springframework.kafka</groupId>\n" +
                    "         <artifactId>spring-kafka</artifactId>\n" +
                    "      </dependency>\n" +
                    "      <dependency>\n" +
                    "         <groupId>org.springframework.boot</groupId>\n" +
                    "         <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                    "      </dependency>\n" +
                    "      <dependency>\n" +
                    "         <groupId>com.h2database</groupId>\n" +
                    "         <artifactId>h2</artifactId>\n" +
                    "         <scope>runtime</scope>\n" +
                    "      </dependency>\n" +
                    "      <dependency>\n" +
                    "         <groupId>org.springframework.boot</groupId>\n" +
                    "         <artifactId>spring-boot-starter-test</artifactId>\n" +
                    "         <scope>test</scope>\n" +
                    "      </dependency>\n" +
                    "      \n" +
                    "      <dependency>\n" +
                    "         <groupId>org.springframework.cloud</groupId>\n" +
                    "         <artifactId>spring-cloud-stream-binder-kafka</artifactId>\n" +
                    "      </dependency>\n" +
                    "      \n" +
                    "      <dependency>\n" +
                    "         <groupId>org.springframework.cloud</groupId>\n" +
                    "         <artifactId>spring-cloud-stream-reactive</artifactId>\n" +
                    "      </dependency>\n" +
                    "      <dependency>\n" +
                    "         <groupId>org.springframework.kafka</groupId>\n" +
                    "         <artifactId>spring-kafka</artifactId>\n" +
                    "      </dependency>\n" +
                    "      \n" +
                    "      <dependency>\n" +
                    "         <groupId>com.google.code.gson</groupId>\n" +
                    "         <artifactId>gson</artifactId>\n" +
                    "         <version>2.8.5</version>\n" +
                    "         <scope>compile</scope>\n" +
                    "      </dependency>\n" +
                    "      \n" +
                    "      <dependency>\n" +
                    "         <groupId>org.apache.commons</groupId>\n" +
                    "         <artifactId>commons-lang3</artifactId>\n" +
                    "         <version>3.8.1</version>\n" +
                    "      </dependency>\n" +
                    "\n" +
                    "      <dependency>\n" +
                    "         <groupId>org.apache.httpcomponents</groupId>\n" +
                    "         <artifactId>httpclient</artifactId>\n" +
                    "         <version>4.3.4</version>\n" +
                    "      </dependency>\n" +
                    "      \n" +
                    "\n" +
                    "   </dependencies>\n" +
                    "\n" +
                    "   <dependencyManagement>\n" +
                    "      <dependencies>\n" +
                    "         <dependency>\n" +
                    "            <groupId>org.springframework.cloud</groupId>\n" +
                    "            <artifactId>spring-cloud-dependencies</artifactId>\n" +
                    "            <version>${spring-cloud.version}</version>\n" +
                    "            <type>pom</type>\n" +
                    "            <scope>import</scope>\n" +
                    "         </dependency>\n" +
                    "      </dependencies>\n" +
                    "   </dependencyManagement>\n" +
                    "\n" +
                    "   <build>\n" +
                    "      <plugins>\n" +
                    "         <plugin>\n" +
                    "            <groupId>org.springframework.boot</groupId>\n" +
                    "            <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                    "         </plugin>\n" +
                    "      </plugins>\n" +
                    "   </build>\n" +
                    "\n" +
                    "</project>";

            String appStr = "server:\n" +
                    "  port: 8080\n" +
                    "\n" +
                    "\n" +
                    "topic:\n" +
                    "  orderTopic: orderTopic\n" +
                    "\n" +
                    "---\n" +
                    "spring:\n" +
                    "  profiles: default\n" +
                    "  kafka:\n" +
                    "    #    bootstrap-servers: http://35.194.123.133:19092\n" +
                    "    bootstrap-servers: localhost:9092\n" +
                    "    consumer:\n" +
                    "      enable-auto-commit: true\n" +
                    "  jpa:\n" +
                    "    properties:\n" +
                    "      hibernate:\n" +
                    "        show_sql: true\n" +
                    "\n" +
                    "logging:\n" +
                    "  level:\n" +
                    "    org:\n" +
                    "      hibernate:\n" +
                    "        type: trace        \n" +
                    "\n" +
                    "---\n" +
                    "spring:\n" +
                    "  profiles: docker\n" +
                    "  kafka:\n" +
                    "    bootstrap-servers: my-kafka.kafka.svc.cluster.local:9092\n" +
                    "    consumer:\n" +
                    "      enable-auto-commit: true";

            String appliccationJavaStr = "package com.example.template;\n" +
                    "\n" +
                    "import org.springframework.boot.SpringApplication;\n" +
                    "import org.springframework.boot.autoconfigure.SpringBootApplication;\n" +
                    "import org.springframework.context.ApplicationContext;\n" +
                    "\n" +
                    "@SpringBootApplication\n" +
                    "public class Application {\n" +
                    "\n" +
                    "    public static ApplicationContext applicationContext;\n" +
                    "    public static void main(String[] args) {\n" +
                    "        applicationContext = SpringApplication.run(Application.class, args);\n" +
                    "    }\n" +
                    "    //실험..\n" +
                    "}";
            String serviceJavaStr = "package com.example.template." + Aggregate + ";\n" +
                    "\n" +
                    "\n" +
                    "import com.fasterxml.jackson.core.JsonProcessingException;\n" +
                    "import com.fasterxml.jackson.databind.DeserializationFeature;\n" +
                    "import com.fasterxml.jackson.databind.ObjectMapper;\n" +
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.http.ResponseEntity;\n" +
                    "import org.springframework.stereotype.Service;\n" +
                    "import org.springframework.web.client.RestTemplate;\n" +
                    "\n" +
                    "import java.io.IOException;\n" +
                    "\n" +
                    "@Service\n" +
                    "public class " + Aggregate + "Service {\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "   \n" +
                    "}\n" +
                    "";
            String repositoryJavaStr = "package com.example.template." + Aggregate + ";\n" +
                    "\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "import org.springframework.data.repository.CrudRepository;\n" +
                    "import org.springframework.data.repository.query.Param;\n" +
                    "\n" +
                    "public interface " + Aggregate + "Repository extends CrudRepository<" + Aggregate + ", Long> {\n" +
                    "   \n" +
                    "}\n" +
                    "";


            byte[] one = DockerStr.getBytes();
            byte[] two = cloudbuildStr.getBytes();
            byte[] three = mvnwStr.getBytes();
            byte[] four = mvnwcmdStr.getBytes();
            byte[] five = pomStr.getBytes();

            byte[] app = appStr.getBytes();

            byte[] appjava = appliccationJavaStr.getBytes();

            byte[] service = serviceJavaStr.getBytes();
            byte[] userReposit = repositoryJavaStr.getBytes();


            DockerfileFile.write(one);
            cloudbuildFile.write(two);
            mvnwFile.write(three);
            mvnwcmdFile.write(four);
            pomFile.write(five);

            applicationFile.write(app);

            applicationJavaFile.write(appjava);

            serviceJavaFile.write(service);
            repositoryJavaFile.write(userReposit);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }


}
