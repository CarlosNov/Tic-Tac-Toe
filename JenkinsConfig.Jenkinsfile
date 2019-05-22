node {
   stage('Preparation') {
      
      if (isUnix())
      {
        sh "cd /home/ais/a.quesada.2016-c.novella.2016"   
      }
      else
      {
          bat """
                cd C:\\ais\\a.quesada.2016-c.novella.2016
            """
      }          
      mvnHome = tool 'M3'
   }
   stage('Test') {
       if(isUnix())
       {
           sh """
                cd /home/ais/a.quesada.2016-c.novella.2016
                mvn test
           """
       }
       else
       {
           bat """
                cd C:\\ais\\a.quesada.2016-c.novella.2016
                mvn test
            """
       }
   }
}