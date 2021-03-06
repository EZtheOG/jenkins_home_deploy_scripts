// Author: Lorenz Vanthillo
// Github: https://github.com/lvthillo/shared-library
// https://medium.com/@lvthillo/send-slack-notifications-in-jenkins-pipelines-using-a-shared-library-873ca876f72c
// ehhh I built ontop of htis
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

def failed() {
  node {
      JSONArray attachments = new JSONArray();
      JSONObject attachment = new JSONObject();

      attachment.put('title','Jenkins has failed you');
      attachment.put('text','Something went wrong!');
      attachment.put('fallback','Ruh Roh!');
      attachment.put('image_url','https://bit.ly/2OG9P3G');
      attachment.put('color','danger');

      attachments.add(attachment);
      slackSend(message: "This job <${env.JOB_URL}${env.JOB_NAME}> (<${env.BUILD_URL}|#${env.BUILD_NUMBER}>)\nHas failed.",
        attachments: attachments.toString())
  }
}

def successful() {
  node {
      JSONArray attachments = new JSONArray();
      JSONObject attachment = new JSONObject();

      attachment.put('title','Jenkins Build was successful');
      attachment.put('text','YAAAAS!');
      attachment.put('fallback','high five!');
      attachment.put('image_url','https://bit.ly/2RKD1bt');
      attachment.put('color','success');

      attachments.add(attachment);
      slackSend(message: "This job <${env.JOB_URL}${env.JOB_NAME}> (<${env.BUILD_URL}|#${env.BUILD_NUMBER}>)\nCompleted.",
        attachments: attachments.toString())
  }
}

def call(String buildResult) {
  if ( buildResult == "SUCCESS" ) {
    successful()
  }
  else if( buildResult == "FAILURE" ) { 
    failed()
  }
    else {
    slackSend color: "danger", message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} its results were unclear"
  }
}
