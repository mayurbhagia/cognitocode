import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClientBuilder;
import com.amazonaws.services.cognitoidentity.model.Credentials;
import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityResult;
import com.amazonaws.services.cognitoidentity.model.GetIdRequest;
import com.amazonaws.services.cognitoidentity.model.GetIdResult;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeRequest;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iot.model.AttachPrincipalPolicyRequest;
import com.amazonaws.util.StringUtils;


public class CognitoUtils {

    private static String clientId = "xxxxxxxxxxxxxxxxxxxxxxxxxx";
    private static String userPoolId = "ap-south-1_xxxxxxxxx";

    private static String userName = "xxxxx";
    private static String userPassword = "xxxxxx";
    private static String newuserPassword = "xxxxxxx";



    public static void main(String args[]) {

        String idToken = getIdToken();
        System.out.println("idToken : " + idToken);
        //getAWSCreds(idToken);
    }

    public static String getIdToken() {

        System.setProperty("aws.accessKeyId", "xxxxx");
        System.setProperty("aws.secretKey", "xxxxx");

        AWSCognitoIdentityProvider provider = AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1).withCredentials(new SystemPropertiesCredentialsProvider()).build();
        Map<String, String> authParams = new HashMap<>();
        System.out.println("Provider========>"+provider);

        authParams.put("USERNAME", userName);
        authParams.put("PASSWORD", newuserPassword);

        AdminInitiateAuthRequest adminInitiateAuthRequest = new AdminInitiateAuthRequest().withClientId(clientId)
                .withUserPoolId(userPoolId).withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withAuthParameters(authParams);

        System.out.println("adminInitiateAuthRequest========>"+adminInitiateAuthRequest);
        AdminInitiateAuthResult result = provider.adminInitiateAuth(adminInitiateAuthRequest);


        System.out.println("result.getChallengeName() : =======>" + result);

        if (StringUtils.isNullOrEmpty(result.getChallengeName())) {
            return "ID token is ====>"+
                    result.getAuthenticationResult().getIdToken() +"\n"+
                    "Refresh token is ====>"+result.getAuthenticationResult().getRefreshToken();
        }

		 else {
			//resetPassword(userName, newuserPassword, result, provider);
            return "abc";
		}





/*
        Map <String,String> challengeResponses = new HashMap<>();
  challengeResponses.put("USERNAME" , userName);
  challengeResponses.put("NEW_PASSWORD", newuserPassword);
  RespondToAuthChallengeRequest respondToAuthChallengeRequest=new RespondToAuthChallengeRequest()
          .withChallengeName("NEW_PASSWORD_REQUIRED")
  .withClientId(clientId) .withChallengeResponses(challengeResponses)
                .withSession(result.getSession());
  provider.respondToAuthChallenge(respondToAuthChallengeRequest);
  return "abc";
*/


    }
/*
	private static void resetPassword(String username, String newPassword, AdminInitiateAuthResult result,
			AWSCognitoIdentityProvider provider) {
		Map<String, String> challengeResponses = new HashMap<>();
		challengeResponses.put("USERNAME", username);
		challengeResponses.put("NEW_PASSWORD", newPassword);

		RespondToAuthChallengeRequest respondToAuthChallengeRequest = new RespondToAuthChallengeRequest()
				.withChallengeName("NEW_PASSWORD_REQUIRED").withClientId(clientId)
				.withChallengeResponses(challengeResponses).withSession(result.getSession());

		provider.respondToAuthChallenge(respondToAuthChallengeRequest);
		System.out.println("passwors reset successfully");
	}
	*/


/*
    public static Map<String, Object> getAWSCreds(String idToken) {
        GetIdRequest idRequest = new GetIdRequest();
        idRequest.setIdentityPoolId(identityPoolId);

        Map<String,String> providerTokens = new HashMap<>();
        providerTokens.put(providerName, idToken);
        idRequest.setLogins(providerTokens);
        System.out.println("providerTokens : " + providerTokens);

        AmazonCognitoIdentity amazonCognitoIdentity = AmazonCognitoIdentityClientBuilder.standard().withCredentials(new SystemPropertiesCredentialsProvider()).withRegion(Regions.US_EAST_1).build();

        GetIdResult idResp = amazonCognitoIdentity.getId(idRequest);

        System.out.println("Identity: " + idResp.getIdentityId());

        GetCredentialsForIdentityResult credentialsForIdentity = amazonCognitoIdentity
                .getCredentialsForIdentity(
                        new GetCredentialsForIdentityRequest()
                                .withIdentityId(idResp.getIdentityId())
                                .withLogins(providerTokens));


        Credentials credentials = credentialsForIdentity.getCredentials();

        System.out.println("credentials.getAccessKeyId() : " + credentials.getAccessKeyId());
        System.out.println("credentials.getSecretKey() : " + credentials.getSecretKey());
        System.out.println("credentials.getSessionToken() : " + credentials.getSessionToken());


        Map<String, Object> results = new HashMap<>();
        results.put("indetityId", idResp.getIdentityId());
        results.put("credentials", credentials);
        return results;

    }

*/

}