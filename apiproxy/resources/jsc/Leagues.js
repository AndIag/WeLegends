var summ_id = context.getVariable('urirequest.id'); //you should get this using context.getVariable()
var region = context.getVariable('urirequest.region');
var path = 'https://' + region + '.api.pvp.net/api/lol/' + region + '/v2.5/league/by-summoner/' + summ_id +'/entry'; //new path
var current_target_url = context.getVariable('target.url'); //get the existing target url
context.setVariable('target.copy.pathsuffix', false); //tell apigee to not copy the path over to the target
context.setVariable('debug.js.path', path); //this line is only for seeing the value in the trace tool
context.setVariable('target.url', path); //set the new path 