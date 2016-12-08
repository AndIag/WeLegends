var match_id = context.getVariable('urirequest.id'); //you should get this using context.getVariable()
var region = context.getVariable('urirequest.region');
var url = 'https://' + region + '.api.pvp.net/api/lol/' + region + '/v2.2/match/' + match_id; //new path
context.setVariable('target.copy.pathsuffix', false); //tell apigee to not copy the path over to the target
context.setVariable('debug.js.path', url); //this line is only for seeing the value in the trace tool
context.setVariable('target.url', url); //set the new path