function quickAction(id) {
  if (id == '0') {
    return true;
  }
//  if (id == 'call') {
//    popURL('ExternalContactsCalls.do?command=Add&actionSource=GlobalItem&popup=true','Call','600','300','yes','yes');
//  }
//  if (id == 'opportunity') {
//    popURL('OpportunityForm.do?command=Prepare&actionSource=GlobalItem','Opportunity','600','500','yes','yes');
//  }
  if (id == 'task') {
    popURL('MyTasks.do?command=New&actionSource=GlobalItem&popup=true','Task','600','425','yes','yes');
  }
  if (id == 'ticket') {
    popURL('TroubleTickets.do?command=Add&actionSource=GlobalItem&popup=true','Ticket','600','500','yes','yes');
  }
//  if (id == 'message') {
//    popURL('MyCFSInbox.do?command=NewMessage&actionSource=GlobalItem&popup=true','Message','700','550','yes','yes');
//  }
}