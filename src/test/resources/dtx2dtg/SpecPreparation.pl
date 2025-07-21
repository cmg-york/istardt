% DT-Golog Specification for Model: Spec 
% Date Translated: 2025-07-21 09:26:41 
% From source: Spec 
% Using DTTRanslate 
:-style_check(-discontiguous).
:-style_check(-singleton).
:- multifile getRewardMode/1.
:- multifile getRewardModeDTG/1.
:- multifile penalizeDeadlock/1.
:- multifile deadlockPenalty/1.
:- multifile getInfeasiblePenalty/1.
:- multifile val/2.
:-dynamic(init/1).



%
% OPTIONS 
% 

getNumRuns(1).
getObsType(continuous).



%
% CROSS-RUN ELEMENTS 
% 

%
% C R O S S   S T A T E 
%
transStateStructure([overallQuality(_)]).




%
% EXPORTED STATE ELEMENTS 
% 

%
% D I S C R E T E   E X P O R T S
%
discreteExportedSet([haveIStarDTXSpecPrepared_Sat]).


%
% C O N T I N U O U S   E X P O R T S
%
continuousExportedSet([[overallQuality(_),-1.0,1.0]]).




%
% INITIALIZATIONS 
% 

init([drawingSuccessful_fl,becomeXMLWizard(0.8),avoidLabor(-0.5)]).




%
% ACTION LISTS 
% 

agentActionList([drawIStarDTVModel,translateMxGraphToIStarDTX,handcraftSpec,translateToDTGolog,visuallyInspectIStarDTXSpec,runSimulation]).

agentAction(drawIStarDTVModel).
agentAction(translateMxGraphToIStarDTX).
agentAction(handcraftSpec).
agentAction(translateToDTGolog).
agentAction(visuallyInspectIStarDTXSpec).
agentAction(runSimulation).

stochasticActionList([drawingSuccessful_Eff,drawingFailed_Eff,specSuccessfullyGenerated_Eff,runFailed_Eff,handcraftingSuccessful_Eff,handcraftingFailed_Eff,translationSuccessful_Eff,translationFailed_Eff,inspectionSuccessful_Eff,inspectionFoundIssues_Eff,simulationSuccessful_Eff,simulationFailed_Eff]).

stochasticAction(drawingSuccessful_Eff).
stochasticAction(drawingFailed_Eff).
stochasticAction(specSuccessfullyGenerated_Eff).
stochasticAction(runFailed_Eff).
stochasticAction(handcraftingSuccessful_Eff).
stochasticAction(handcraftingFailed_Eff).
stochasticAction(translationSuccessful_Eff).
stochasticAction(translationFailed_Eff).
stochasticAction(inspectionSuccessful_Eff).
stochasticAction(inspectionFoundIssues_Eff).
stochasticAction(simulationSuccessful_Eff).
stochasticAction(simulationFailed_Eff).

nondetActions(drawIStarDTVModel,_,[drawingSuccessful_Eff,drawingFailed_Eff]).
nondetActions(translateMxGraphToIStarDTX,_,[specSuccessfullyGenerated_Eff,runFailed_Eff]).
nondetActions(handcraftSpec,_,[handcraftingSuccessful_Eff,handcraftingFailed_Eff]).
nondetActions(translateToDTGolog,_,[translationSuccessful_Eff,translationFailed_Eff]).
nondetActions(visuallyInspectIStarDTXSpec,_,[inspectionSuccessful_Eff,inspectionFoundIssues_Eff]).
nondetActions(runSimulation,_,[simulationSuccessful_Eff,simulationFailed_Eff]).

prob(drawingSuccessful_Eff,0.95,_).
prob(drawingFailed_Eff,0.05,_).
prob(specSuccessfullyGenerated_Eff,0.95,_).
prob(runFailed_Eff,0.05,_).
prob(handcraftingSuccessful_Eff,0.8,_).
prob(handcraftingFailed_Eff,0.2,_).
prob(translationSuccessful_Eff,0.9,_).
prob(translationFailed_Eff,0.1,_).
prob(inspectionSuccessful_Eff,0.9,_).
prob(inspectionFoundIssues_Eff,0.1,_).
prob(simulationSuccessful_Eff,0.9,_).
prob(simulationFailed_Eff,0.1,_).



%
% PROCEDURES 
% 

proc(simulateDomain, haveIStarDTXSpecPrepared : haveDTGologSpecPrepared).
proc(haveIStarDTXSpecPrepared, haveSpecVisuallyPrepared # handcraftSpec).
proc(haveSpecVisuallyPrepare).
proc(haveDTGologSpecPrepare).
dtgRun :- write('Policy: '), bp(simulateDomain,10,_,U,P,x),
        write('Utility: '),writeln(U), 
        write('Probability: '),writeln(P).


%
% FLUENT LISTS 
% 

fluentList([drawingSuccessful_fl,drawingFailed_fl,specCreated_fl,happyModeler_fl,runFailed_fl,handcraftingSuccessful_fl,handcraftingFailed_fl,translationSuccessful_fl,translationFailed_fl,inspectionSuccessful_fl,inspectionFoundIssues_fl,simulationSuccessful_fl,simulationFailed_fl]).

%
% SUCCESSOR STATE AXIOMS 
% 

drawingSuccessful_fl(do(A,S)) :- drawingSuccessful_fl(S); A=drawingSuccessful_Eff.
drawingFailed_fl(do(A,S)) :- drawingFailed_fl(S); A=drawingFailed_Eff.
specCreated_fl(do(A,S)) :- specCreated_fl(S); A=specSuccessfullyGenerated_Eff.
happyModeler_fl(do(A,S)) :- happyModeler_fl(S); A=specSuccessfullyGenerated_Eff.
runFailed_fl(do(A,S)) :- runFailed_fl(S); A=runFailed_Eff.
handcraftingSuccessful_fl(do(A,S)) :- handcraftingSuccessful_fl(S); A=handcraftingSuccessful_Eff.
handcraftingFailed_fl(do(A,S)) :- handcraftingFailed_fl(S); A=handcraftingFailed_Eff.
translationSuccessful_fl(do(A,S)) :- translationSuccessful_fl(S); A=translationSuccessful_Eff.
translationFailed_fl(do(A,S)) :- translationFailed_fl(S); A=translationFailed_Eff.
inspectionSuccessful_fl(do(A,S)) :- inspectionSuccessful_fl(S); A=inspectionSuccessful_Eff.
inspectionFoundIssues_fl(do(A,S)) :- inspectionFoundIssues_fl(S); A=inspectionFoundIssues_Eff.
simulationSuccessful_fl(do(A,S)) :- simulationSuccessful_fl(S); A=simulationSuccessful_Eff.
simulationFailed_fl(do(A,S)) :- simulationFailed_fl(S); A=simulationFailed_Eff.

%
% PRECONDITION AXIOMS 
% 

poss(drawingSuccessful_Eff,S) :- \+ drawIStarDTVModel_Att(S).
poss(drawingFailed_Eff,S) :- \+ drawIStarDTVModel_Att(S).
poss(specSuccessfullyGenerated_Eff,S) :- \+ translateMxGraphToIStarDTX_Att(S).
poss(runFailed_Eff,S) :- \+ translateMxGraphToIStarDTX_Att(S).
poss(handcraftingSuccessful_Eff,S) :- \+ handcraftSpec_Att(S),\+ haveSpecVisuallyPrepared_Att(S).
poss(handcraftingFailed_Eff,S) :- \+ handcraftSpec_Att(S),\+ haveSpecVisuallyPrepared_Att(S).
poss(translationSuccessful_Eff,S) :- \+ translateToDTGolog_Att(S).
poss(translationFailed_Eff,S) :- \+ translateToDTGolog_Att(S).
poss(inspectionSuccessful_Eff,S) :- \+ visuallyInspectIStarDTXSpec_Att(S).
poss(inspectionFoundIssues_Eff,S) :- \+ visuallyInspectIStarDTXSpec_Att(S).
poss(simulationSuccessful_Eff,S) :- \+ runSimulation_Att(S).
poss(simulationFailed_Eff,S) :- \+ runSimulation_Att(S).


poss(drawIStarDTVModel,S) :- (poss(drawingSuccessful_Eff,S);poss(drawingFailed_Eff,S)).
poss(translateMxGraphToIStarDTX,S) :- (poss(specSuccessfullyGenerated_Eff,S);poss(runFailed_Eff,S)).
poss(handcraftSpec,S) :- (poss(handcraftingSuccessful_Eff,S);poss(handcraftingFailed_Eff,S)).
poss(translateToDTGolog,S) :- (poss(translationSuccessful_Eff,S);poss(translationFailed_Eff,S)).
poss(visuallyInspectIStarDTXSpec,S) :- (poss(inspectionSuccessful_Eff,S);poss(inspectionFoundIssues_Eff,S)).
poss(runSimulation,S) :- (poss(simulationSuccessful_Eff,S);poss(simulationFailed_Eff,S)).

%
% SATISFACTION FORMULAE 
% 

drawIStarDTVModel_Sat(S) :- drawingSuccessful_fl(S).
translateMxGraphToIStarDTX_Sat(S) :- specCreated_fl(S);happyModeler_fl(S).
handcraftSpec_Sat(S) :- handcraftingSuccessful_fl(S).
translateToDTGolog_Sat(S) :- translationSuccessful_fl(S).
visuallyInspectIStarDTXSpec_Sat(S) :- inspectionSuccessful_fl(S).
runSimulation_Sat(S) :- simulationSuccessful_fl(S).
simulateDomain_Sat(S) :- haveIStarDTXSpecPrepared_Sat(S),haveDTGologSpecPrepared_Sat(S).
haveIStarDTXSpecPrepared_Sat(S) :- haveSpecVisuallyPrepared_Sat(S);handcraftSpec_Sat(S).
haveSpecVisuallyPrepared_Sat(S) :-.
haveDTGologSpecPrepared_Sat(S) :-.


 % Condition Box Related
haveDTGologSpecPreparedANDHaveIStarDTXSpecPrepared_fl(s0) :- !,initiallyTrue(haveDTGologSpecPreparedANDHaveIStarDTXSpecPrepared_fl).
haveDTGologSpecPreparedANDHaveIStarDTXSpecPrepared_fl(S) :- (,).
labeledPrecondition_fl(s0) :- !,initiallyTrue(labeledPrecondition_fl).
labeledPrecondition_fl(S) :- (handcraftingSuccessful_fl(S);specCreated_fl(S)).

%
% ATTEMPT FORMULAE 
% 

drawIStarDTVModel_Att(S) :- drawingSuccessful_fl(S);drawingFailed_fl(S).
translateMxGraphToIStarDTX_Att(S) :- specCreated_fl(S);happyModeler_fl(S);runFailed_fl(S).
handcraftSpec_Att(S) :- handcraftingSuccessful_fl(S);handcraftingFailed_fl(S).
translateToDTGolog_Att(S) :- translationSuccessful_fl(S);translationFailed_fl(S).
visuallyInspectIStarDTXSpec_Att(S) :- inspectionSuccessful_fl(S);inspectionFoundIssues_fl(S).
runSimulation_Att(S) :- simulationSuccessful_fl(S);simulationFailed_fl(S).
simulateDomain_Att(S) :- haveIStarDTXSpecPrepared_Att(S);haveDTGologSpecPrepared_Att(S).
haveIStarDTXSpecPrepared_Att(S) :- haveSpecVisuallyPrepared_Att(S);handcraftSpec_Att(S).
haveSpecVisuallyPrepared_Att(S) :-.
haveDTGologSpecPrepared_Att(S) :-.

%
% ROOT SATISFACTION 
% 

goalAchieved(S) :- simulateDomain_Sat(S).

%
% REWARD FORMULAE 
% 

avoidLabor(V_init,s0) :- getInitValue(avoidLabor,V_init),!.
avoidLabor(V,S) :- 
                    val(R_haveSpecVisuallyPrepared_Sat,haveSpecVisuallyPrepared_Sat(S)),
                    V is 
(0.8) * (R_haveSpecVisuallyPrepared_Sat).


becomeXMLWizard(V_init,s0) :- getInitValue(becomeXMLWizard,V_init),!.
becomeXMLWizard(V,S) :- 
                         val(R_handcraftSpec_Sat,handcraftSpec_Sat(S)),
                         V is 
(0.7) * (R_handcraftSpec_Sat).


overallQuality(V_init,s0) :- getInitValue(overallQuality,V_init),!.
overallQuality(V,S) :-overallQuality(R_overallQuality_init,s0),
                        avoidLabor(R_avoidLabor,S),
                        becomeXMLWizard(R_becomeXMLWizard,S),
                        modelerHappiness(R_modelerHappiness,S),
                        V is R_overallQuality_init +
(((0.4) * (R_avoidLabor)) + ((0.4) * (R_becomeXMLWizard))) + ((0.2) * (R_modelerHappiness)).


modelerHappiness(V_init,s0) :- getInitValue(modelerHappiness,V_init),!.
modelerHappiness(V,S) :- 
                          val(R_happyModeler_fl,happyModeler_fl(S)),
parseSimpleQualityExpressionPart1 warning: type of |Unknown PredicateID| is: class ca.yorku.cmg.istardt.xmlparser.objects.Formula$1                          V is 
((0.2) * (R_happyModeler_fl)) + ((0.7) * (parseSimpleQualityExpressionPart2 warning: type of |Unknown PredicateID| is: class ca.yorku.cmg.istardt.xmlparser.objects.Formula$1)).



rewardInst(R,S) :- overallQuality(R,S).


%
% SENSE CONDITIONS 
% 

senseCondition(drawingSuccessful_Eff,drawingSuccessful_Eff_fl).
senseCondition(drawingFailed_Eff,drawingFailed_Eff_fl).
senseCondition(specSuccessfullyGenerated_Eff,specSuccessfullyGenerated_Eff_fl).
senseCondition(specSuccessfullyGenerated_Eff,specSuccessfullyGenerated_Eff_fl).
senseCondition(runFailed_Eff,runFailed_Eff_fl).
senseCondition(handcraftingSuccessful_Eff,handcraftingSuccessful_Eff_fl).
senseCondition(handcraftingFailed_Eff,handcraftingFailed_Eff_fl).
senseCondition(translationSuccessful_Eff,translationSuccessful_Eff_fl).
senseCondition(translationFailed_Eff,translationFailed_Eff_fl).
senseCondition(inspectionSuccessful_Eff,inspectionSuccessful_Eff_fl).
senseCondition(inspectionFoundIssues_Eff,inspectionFoundIssues_Eff_fl).
senseCondition(simulationSuccessful_Eff,simulationSuccessful_Eff_fl).
senseCondition(simulationFailed_Eff,simulationFailed_Eff_fl).

%
% RESTORE SITUATION ARGUMENT 
% 

restoreSitArg(drawingSuccessful_fl,S,drawingSuccessful_fl(S)).
restoreSitArg(drawingFailed_fl,S,drawingFailed_fl(S)).
restoreSitArg(drawIStarDTVModel_Sat,S,drawIStarDTVModel_Sat(S)).
restoreSitArg(drawIStarDTVModel_Att,S,drawIStarDTVModel_Att(S)).
restoreSitArg(specCreated_fl,S,specCreated_fl(S)).
restoreSitArg(happyModeler_fl,S,happyModeler_fl(S)).
restoreSitArg(runFailed_fl,S,runFailed_fl(S)).
restoreSitArg(translateMxGraphToIStarDTX_Sat,S,translateMxGraphToIStarDTX_Sat(S)).
restoreSitArg(translateMxGraphToIStarDTX_Att,S,translateMxGraphToIStarDTX_Att(S)).
restoreSitArg(handcraftingSuccessful_fl,S,handcraftingSuccessful_fl(S)).
restoreSitArg(handcraftingFailed_fl,S,handcraftingFailed_fl(S)).
restoreSitArg(handcraftSpec_Sat,S,handcraftSpec_Sat(S)).
restoreSitArg(handcraftSpec_Att,S,handcraftSpec_Att(S)).
restoreSitArg(translationSuccessful_fl,S,translationSuccessful_fl(S)).
restoreSitArg(translationFailed_fl,S,translationFailed_fl(S)).
restoreSitArg(translateToDTGolog_Sat,S,translateToDTGolog_Sat(S)).
restoreSitArg(translateToDTGolog_Att,S,translateToDTGolog_Att(S)).
restoreSitArg(inspectionSuccessful_fl,S,inspectionSuccessful_fl(S)).
restoreSitArg(inspectionFoundIssues_fl,S,inspectionFoundIssues_fl(S)).
restoreSitArg(visuallyInspectIStarDTXSpec_Sat,S,visuallyInspectIStarDTXSpec_Sat(S)).
restoreSitArg(visuallyInspectIStarDTXSpec_Att,S,visuallyInspectIStarDTXSpec_Att(S)).
restoreSitArg(simulationSuccessful_fl,S,simulationSuccessful_fl(S)).
restoreSitArg(simulationFailed_fl,S,simulationFailed_fl(S)).
restoreSitArg(runSimulation_Sat,S,runSimulation_Sat(S)).
restoreSitArg(runSimulation_Att,S,runSimulation_Att(S)).
restoreSitArg(simulateDomain_Sat,S,simulateDomain_Sat(S)).
restoreSitArg(simulateDomain_Att,S,simulateDomain_Att(S)).
restoreSitArg(haveIStarDTXSpecPrepared_Sat,S,haveIStarDTXSpecPrepared_Sat(S)).
restoreSitArg(haveIStarDTXSpecPrepared_Att,S,haveIStarDTXSpecPrepared_Att(S)).
restoreSitArg(haveSpecVisuallyPrepared_Sat,S,haveSpecVisuallyPrepared_Sat(S)).
restoreSitArg(haveSpecVisuallyPrepared_Att,S,haveSpecVisuallyPrepared_Att(S)).
restoreSitArg(haveDTGologSpecPrepared_Sat,S,haveDTGologSpecPrepared_Sat(S)).
restoreSitArg(haveDTGologSpecPrepared_Att,S,haveDTGologSpecPrepared_Att(S)).
restoreSitArg(avoidLabor(X),S,avoidLabor(X,S)).
restoreSitArg(becomeXMLWizard(X),S,becomeXMLWizard(X,S)).
restoreSitArg(overallQuality(X),S,overallQuality(X,S)).
restoreSitArg(modelerHappiness(X),S,modelerHappiness(X,S)).
restoreSitArg(haveDTGologSpecPreparedANDHaveIStarDTXSpecPrepared_fl,S,haveDTGologSpecPreparedANDHaveIStarDTXSpecPrepared_fl(S)).
restoreSitArg(labeledPrecondition_fl,S,labeledPrecondition_fl(S)).

