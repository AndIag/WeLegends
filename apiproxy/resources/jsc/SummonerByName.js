var summ_name = context.getVariable('urirequest.id'); //you should get this using context.getVariable()
var region = context.getVariable('urirequest.region');
var path = 'https://' + region + '.api.pvp.net/api/lol/' + region + '/v1.4/summoner/by-name/' + summ_name; //new path
var current_target_url = context.getVariable('target.url'); //get the existing target url
context.setVariable('target.copy.pathsuffix', false); //tell apigee to not copy the path over to the target
context.setVariable('debug.js.path', path); //this line is only for seeing the value in the trace tool
context.setVariable('target.url', path); //set the new path