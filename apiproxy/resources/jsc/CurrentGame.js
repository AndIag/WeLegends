var summ_id = context.getVariable('urirequest.id'); //you should get this using context.getVariable()
var region = context.getVariable('urirequest.region');
var platform = context.getVariable('urirequest.platform')
var path = 'https://' + region + '.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/' + platform + '/' + summ_id; //new path
var current_target_url = context.getVariable('target.url'); //get the existing target url
context.setVariable('target.copy.pathsuffix', false); //tell apigee to not copy the path over to the target
context.setVariable('debug.js.path', path); //this line is only for seeing the value in the trace tool
context.setVariable('target.url', path); //set the new path 