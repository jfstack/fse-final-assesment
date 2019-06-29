import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LogService {

  level: LogLevel = LogLevel.OFF;
  logWithDate: boolean = true;

  constructor() { }

  debug(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.DEBUG,
                    optionalParams);
  }
        
  info(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.INFO,
                    optionalParams);
  }
          
  error(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.ERROR,
                    optionalParams);
  }

  private shouldLog(level: LogLevel): boolean {
    let ret: boolean = false;
    if ((level >= this.level &&
         level !== LogLevel.OFF) ||
         this.level === LogLevel.ALL) {
      ret = true;
    }
    return ret;
  }

  private writeToLog(msg: string, level: LogLevel, params: any[]) {
    if (this.shouldLog(level)) {
      let entry: LogEntry = new LogEntry();
      entry.message = msg;
      entry.level = level;
      entry.extraInfo = params;
      entry.logWithDate = this.logWithDate;
      console.log(entry.buildLogString());
    }
  }
  
}

export enum LogLevel {
  ALL = 0,
  DEBUG = 1,
  INFO = 2,
  ERROR = 3,
  OFF = 10
}

export class LogEntry {
  // Public Properties
  entryDate: Date = new Date();
  message: string = "";
  level: LogLevel = LogLevel.DEBUG;
  extraInfo: any[] = [];
  logWithDate: boolean = true;
      
  buildLogString(): string {
    let ret: string = "";
      
    if (this.logWithDate) {
      ret = new Date() + " - ";
    }
    ret += "Type: " + LogLevel[this.level];
    ret += " - Message: " + this.message;
    if (this.extraInfo.length) {
      ret += " - Extra Info: "
        + this.formatParams(this.extraInfo);
    }
      
    return ret;
  }
      
  private formatParams(params: any[]): string {
    let ret: string = params.join(",");
      
    // Is there at least one object in the array?
    if (params.some(p => typeof p == "object")) {
      ret = "";
      // Build comma-delimited string
      for (let item of params) {
        ret += JSON.stringify(item) + ",";
      }
    }
      
    return ret;
  }
}
