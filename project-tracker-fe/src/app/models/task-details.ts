import { User } from './user';
import { ParentTask } from 'src/app/models/parent-task';

export class TaskDetails {
    taskId: number;
    task: string;
    startDate: string;
    endDate: string;
    priority: number;
    status: string;
    parentTask: ParentTask;
    owner: User;
    projectId: number;
    projectName: string;
}
