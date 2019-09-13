%% ferpa.pro
%% Formalization of FERPA legislation

%% ------------------------
%% Base relations
%%

%% ferpa_datasetInScope(ds)
%%   Relation holds if the FERPA legislation says that depositor dd and  data set
%%   ds are in the scope of the legislation, i.e., the dataset contains >0 record(s)
%%   that include information from the education records of an educational agency or
%%   institution receiving funds from the US Department of Education.

%% ferpa_pii(ds)
%%   Relation holds if the FERPA legislation says that data set ds contains
%%   personally identifiable information. Specifically, if it contains >0 record(s) that
%%   include:
%%   (a) a student’s name,
%%   (b) the name of a student’s family member,
%%   (c) the address of a student or a student’s family,
%%   (d) other direct identifiers (such as Social Security number, student
%%       number, telephone number, e-mail address, or biometric record),
%%   (e) indirect identifiers (such as gender, race, religion, date of birth,
%%       place of birth, geographic indicators, height, weight, mother’s
%%       maiden name, activities, employment information, education information,
%%       financial information, or other descriptors),
%%   (f) other information that, alone or in combination, is linked or linkable
%%       to a specific student that would allow a reasonable person in the school
%%       community, who does not have personal knowledge of the relevant
%%       circumstances, to identify the student with reasonable certainty, OR
%%   (g) information disclosed in response to a targeted request

%% ferpa_directoryInfo(ds)
%%   Relation holds if the dataset contains >0 record(s) that include information
%%   designated as directory information by the educational agency or institution
%%   that maintained the information.

%% ferpa_allConsented(ds)
%%   Relation holds if for each record in the dataset, the record regards only
%%   students who have given signed consent.

%% ferpa_studiesException(ds)
%%   Relation holds if for each record in the dataset, the record was contributed to the dataset
%%   pursuant to a DUA expressly authorizing release under the FERPA studies exception. (If there
%%   were multiple sharing steps from the educational record to the dataset ds, then each sharing step
%%   was pursuant to a DUA expressly authorizing release under the FERPA studies exception.)

%% ferpa_auditException(ds)
%%   Relation holds if for each record in the dataset, the record was contributed to the dataset
%%   pursuant to a DUA expressly authorizing release under the FERPA audit and evaluation exception. (If there
%%   were multiple sharing steps from the educational record to the dataset ds, then each sharing step
%%   was pursuant to a DUA expressly authorizing release under the FERPA audit and evaluation exception.)

%% ------------------------
%% Constants
%%

%% ferpa_license_notice
%%    License states that the data contain records protected by FERPA

%% ferpa_license_purpose
%%    License states the purpose of the study to be conducted

%% ferpa_license_scope
%%    License states the scope of the proposed study

%% ferpa_license_duration
%%    License states the duration of the study

%% ferpa_license_information
%%    License states the information contained in the dataset

%% ferpa_license_authorizedRepresentative
%%    License designates the Data Recipient as an authorized representative of a FERPA-covered entity

%% ferpa_license_irb
%%    “Data Recipient has obtained from Data Recipient’s IRB, either approval or a determination of exemption for all research conducted using confidential data, where required by law and/or Data Recipient’s IRB policy.”
%%    “Data Provider may request a copy of any completed IRB review related to the data, and Data Recipient shall provide Data Provider with a copy of the requested IRB review within 10  days of the request.”

%% ferpa_license_auditException
%%    “Personally identifiable information may only be used to carry out an audit or evaluation of Federal- or State-supported education programs, or for the enforcement of or compliance with, Federal legal requirements related to these programs.”
%%    “Approval to use PII for one audit or evaluation does not confer approval to use it for another.”

%% ferpa_license_authorizedUse
%%    "Data Recipient must protect PII from further disclosures or other uses, except as authorized by the Data Provider in accordance with FERPA."


%% general_license_researchProposal
%%    “Data Recipient may use and disclose PII only for the purposes described in the research proposal.”


%% general_license_minimumPersonnel
%%    “Data Recipient attests that only relevant individuals will have access to the PII in order to perform the work.”

%% general_license_minimumInformation
%%    “Data Recipient attests that the PII requested represents the minimum necessary information for the work as described in the research proposal.”


%% general_license_dataDestruction
%%     “Data Recipient must destroy records in a secure manner or return records to Data Provider at the end of the work described in the research proposal or within _____ days, whichever is sooner.”

%% general_license_regulatoryCompliance
%%    “Data Recipient may use and disclose PII only in a manner that does not violate local or federal privacy regulations.”


%% general_license_appropriateSecurity
%%    “Data Recipient must implement appropriate administrative, technical, and physical safeguards to protect the data from unauthorized use or disclosure.”




%% ------------------------
%% Derived relations
%%


%% ferpa_identifiable(DS)
%%   Relation holds if the dataset is in the scope of FERPA and contains PII according to FERPA.
ferpa_identifiable(DS) :-
    ferpa_datasetInScope(DS),
    ferpa_pii(DS).

%% Have an explicit representation of FERPA's notion of "not identifiable", since there are other ways that it might hold
:- multifile(ferpa_not_identifiable/1).
:- dynamic(ferpa_not_identifiable/1).
ferpa_not_identifiable(DS) :-
    \+(ferpa_identifiable(DS)).


ferpa_acceptable_license_IRB(CS) :-
    conditionsRequire(CS, ferpa_license_notice),
    conditionsRequire(CS, ferpa_license_purpose),
    conditionsRequire(CS, ferpa_license_scope),
    conditionsRequire(CS, ferpa_license_duration),
    conditionsRequire(CS, ferpa_license_information),
    conditionsRequire(CS, ferpa_license_irb).

ferpa_acceptable_license_studiesException(CS) :-
    conditionsRequire(CS, ferpa_license_notice),
    conditionsRequire(CS, ferpa_license_purpose),
    conditionsRequire(CS, ferpa_license_scope),
    conditionsRequire(CS, ferpa_license_duration),
    conditionsRequire(CS, ferpa_license_information),
    conditionsRequire(CS, general_license_researchProposal),
    conditionsRequire(CS, general_license_minimumPersonnel),
    conditionsRequire(CS, general_license_minimumInformation),
    conditionsRequire(CS, general_license_dataDestruction).

ferpa_acceptable_license_auditException(CS) :-
    conditionsRequire(CS, ferpa_license_notice),
    conditionsRequire(CS, ferpa_license_authorizedRepresentative),
    conditionsRequire(CS, ferpa_license_information),
    conditionsRequire(CS, ferpa_license_purpose),
    conditionsRequire(CS, ferpa_license_auditException),
    conditionsRequire(CS, ferpa_license_authorizedUse),
    conditionsRequire(CS, general_license_researchProposal),
    conditionsRequire(CS, general_license_regulatoryCompliance),
    conditionsRequire(CS, general_license_appropriateSecurity),
    conditionsRequire(CS, general_license_minimumPersonnel),
    conditionsRequire(CS, general_license_minimumInformation),
    conditionsRequire(CS, general_license_dataDestruction).

ferpa_acceptable_license_deposit(CS) :-
    conditionsRequire(CS, ferpa_license_notice).


%% ---------------------------------------------
%% inScope rules. Does ferpa claim that an action is in scope?

inScope(ferpa, A) :-
    dataSetForAction(A, _R, DS, _DD),
    ferpa_datasetInScope(DS).



%% ---------------------------------------------
%% FERPA rules for permitted and denied actions
%%

%% DEPOSIT
permitted(ferpa, deposit(_DD, DS, _R, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_not_identifiable(DS).

permitted(ferpa, deposit(_DD, DS, _R, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_identifiable(DS),
    ferpa_allConsented(DS),
    ferpa_acceptable_license_deposit(CS).

permitted(ferpa, deposit(_DD, DS, _R, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_identifiable(DS),
    (ferpa_studiesException(DS) ; ferpa_auditException(DS)),
    ferpa_acceptable_license_deposit(CS).

denied(ferpa, deposit(_DD, DS, _R, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_identifiable(DS),
    \+(ferpa_allConsented(DS)),
    \+(ferpa_studiesException(DS)),
    \+(ferpa_auditException(DS)).


%% ACCEPT

permitted(ferpa, accept(R, DS, DD, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    \+(ferpa_identifiable(DS)).

permitted(ferpa, accept(R, DS, DD, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_identifiable(DS),
    ferpa_allConsented(DS),
    ferpa_acceptable_license_deposit(CS).

permitted(ferpa, accept(R, DS, DD, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_identifiable(DS),
    (ferpa_studiesException(DS) ; ferpa_auditException(DS)),
    ferpa_acceptable_license_deposit(CS).

denied(ferpa, accept(R, DS, DD, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_identifiable(DS),
    \+(ferpa_allConsented(DS)),
    \+(ferpa_studiesException(DS)),
    \+(ferpa_auditException(DS)).


%% RELEASE

permitted(ferpa, release(_R, DS, _DU, _DD, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_not_identifiable(DS).

%% release is permitted so long as it is studies or audit exception, and license is either studies or audit exception (i.e.,
%% a studies exception dataset can be released under the audit exception, and vice versa).
permitted(ferpa, release(_R, DS, _DU, _DD, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_identifiable(DS),
    (ferpa_studiesException(DS); ferpa_auditException(DS)),
    (ferpa_acceptable_license_studiesException(CS); ferpa_acceptable_license_auditException(CS)).

permitted(ferpa, release(_R, DS, _DU, _DD, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_identifiable(DS),
    ferpa_allConsented(DS),
    ferpa_acceptable_license_IRB(CS).

% Also permit release if DS is derived using differential privacy
% from abother dataset, DS2, and the total budget used in
% all releases of the data (EPS) is suitable
% (i.e., less than ferpaSufficientEpsBudget
permitted(ferpa, release(_R, DS, _DU, _DD, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    derivedFrom(DS, _, differentialPrivacy(Params)),
    member([totalBudget, EPS], Params),
    ferpaSufficientEps(FE),
    EPS =< FE.

denied(ferpa, release(_R, DS, _DU, _DD, CS), N) :-
    bounded(CS, N),
    ferpa_datasetInScope(DS),
    ferpa_identifiable(DS),
    \+(ferpa_allConsented(DS)),
    \+(ferpa_studiesException(DS)),
    \+(ferpa_auditException(DS)).
