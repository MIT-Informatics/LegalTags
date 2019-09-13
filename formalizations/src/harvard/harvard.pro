%% harvard.pro
%% Example formalization of local module for Harvard. This is not
%% an accurate reflection of Harvard University's data handling
%% policies.

%% In this example module, Harvard policy is to comply with
%% the FERPA and CMR formalizations.
%% This means that an action A is permitted by Harvard if
%% every law that is in scope permits it.
%% and is
%% denied by Harvard if it is
%% denied by either FERPA or CMR.
permitted(harvard, A, N) :-
    (permitted(ferpa, A, N); \+inScope(ferpa, A)),
    (permitted(cmr, A, N); \+inScope(cmr, A)).

denied(harvard, A, N) :-
    denied(ferpa, A, N); denied(cmr, A, N).


%% For the purposes of this example module, we will regard every action as being in scope.
%% This means that there are actions that Harvard regards as in scope, but does not
%% either permit nor deny. Depending on the Harvard's policy, we may want to deny any
%% action that is not explicitly permitted. We do not include that rule here,
%% but it would be denied(harvard, A, N) :- \+permitted(harvard, A, N).
inScope(harvard, A).

%% Set the level of Sufficient budget for release under FERPA.
%% See the FERPA formalization.
ferpaSufficientEpsBudget(0.1).

%% Indicate that the PSI tool is a differentially private tool,
%% i.e., if we use the PSI tool to derive DS from DS2, then
%% we regard the deriviation as being differentially private.
derivedFrom(DS, DS2, differentialPrivacy(Params)) :- 
    derivedFrom(DS, DS2, psiTool(Params)).
