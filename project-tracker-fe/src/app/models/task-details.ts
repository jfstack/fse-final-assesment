import { User } from './user';

export class TaskDetails {
    taskId: number;
    task: string;
    startDate: string;
    endDate: string;
    priority: number;
    status: string;
    parentTask: string;
    owner: User;
}
