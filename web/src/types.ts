export type LanguageMode = 'hi' | 'en';

export type IssueScope = 'WARD_VILLAGE' | 'CITY_BLOCK' | 'DISTRICT' | 'STATE_NATIONAL';

export type CampaignThemeId = 
  | 'URBAN_CIVIC' 
  | 'RURAL_DEV' 
  | 'GOVERNANCE' 
  | 'EDU_HEALTH' 
  | 'LEGAL_RIGHTS';

export type SocialPlatformId = 
  | 'x' 
  | 'instagram' 
  | 'facebook' 
  | 'linkedin' 
  | 'pressRelease' 
  | 'policyBrief';

export type ReportTone = 'INVESTIGATIVE' | 'CONSTRUCTIVE' | 'STORYTELLING' | 'URGENT';

export type EventTypeId = 
  | 'NONE'
  | 'JAN_SUNWAI' 
  | 'GRAM_SABHA' 
  | 'OFFICIAL_INSPECTION' 
  | 'CITIZEN_PRESS_CONFERENCE' 
  | 'PROTEST_DHARNA' 
  | 'COMMUNITY_MEETING';

export type ReportStatus = 'UNDER_INVESTIGATION' | 'IN_REVIEW' | 'RESOLVED';

export type NavigationTab = 
  | 'dashboard' 
  | 'new-intake' 
  | 'ground-report' 
  | 'social-deck' 
  | 'visual-studio' 
  | 'library' 
  | 'calendar' 
  | 'analytics' 
  | 'brand-settings';

export interface EventDetails {
  eventType: EventTypeId;
  title: string;
  date: string;
  venue: string;
  isUpcoming: boolean;
}

export interface SearchCitation {
  title: string;
  url: string;
}

export interface GeneratedPostBundle {
  x: string;
  instagram: string;
  facebook: string;
  linkedin: string;
  pressRelease: string;
  policyBrief: string;
}

export interface ReportFormState {
  category: CampaignThemeId;
  scope: IssueScope;
  headline: string;
  localUnit: string;
  subDistrict: string;
  district: string;
  state: string;
  department: string;
  officialClaim: string;
  groundReality: string;
  citizenQuotes: string;
  affectedPopulation: string;
  evidenceNotes: string;
  actionDemands: string;
  deadline: string;
  relevantOfficial: string;
}

export interface SavedReport {
  id: string;
  createdAt: number;
  status: ReportStatus;
  formData: ReportFormState;
  generatedReport: string;
  socialBundle: GeneratedPostBundle;
}
