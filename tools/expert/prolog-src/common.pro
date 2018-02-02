%% common.pro
%% Common relations and rules for the Legal Tags framework

%% licenseRequires(l, ad).
%% This relation holds if the license l requires action description ad.

%% conditionsRequire(cs, ad).
%% This relation holds if condition set cs requires action description ad.
conditionsRequire(CS, AD) :-
    member(license(L), CS),
    licenseRequires(L, AD).

conditionsRequire(CS, AD) :-
    member(AD, CS).

%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Utility functions

% Make sure that list L is at most length N
bounded(L,N) :-
  between(0,N,C),
  length(L,C).

% Extract the condition set from an action
conditionSetForAction(release(_R, _DS, _DU, _DD, CS), CS).
conditionSetForAction(deposit(_DD, _DS, _R, CS), CS).
conditionSetForAction(accept(_R, _DS, _DD, CS), CS).

% Extract the dataset from an action
dataSetForAction(release(R, DS, _DU, DD, _CS), R, DS, DD).
dataSetForAction(deposit(DD, DS, R, _CS), R, DS, DD).
dataSetForAction(accept(R, DS, DD, _CS), R, DS, DD).

